package com.utc.formut.web.services;

import com.utc.formut.configuration.errors.Error;
import com.utc.formut.web.dto.*;
import com.utc.formut.web.models.*;
import com.utc.formut.web.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private JourneyAnswerRepository journeyAnswerRepository;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private  FormService formService;


    @Value("${spring.mail.username}")
    private String host;


    ModelMapper modelMapper = new ModelMapper();

    public Object getJourneysOfUser(Long id) {
        List<Journey> list = journeyRepository.findAllByUser(userRepository.getById(id));
        List<JourneyDTO> listDTO = new ArrayList<>();

        if(list.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Error("No journey found for this user."));

        for (Journey journey: list) {
            listDTO.add(modelMapper.map(journey, JourneyDTO.class));
        }

        return listDTO;
    }

    public Object getAllJourneys() {
        List<Journey> list = journeyRepository.findAll();
        List<JourneyDTO> listDTO = new ArrayList<>();

        if(list.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Error("No journey."));

        for (Journey journey: list) {
            listDTO.add(modelMapper.map(journey, JourneyDTO.class));
        }

        return listDTO;
    }

    public ResponseEntity<?> getUser(Long id) {
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new Error("User already exists"));

        return ResponseEntity.ok(modelMapper.map(optUser.get(), UserDTO.class));
    }

    public User getUserByMail(String mail) {
        if(userRepository.findByMail(mail).isEmpty()) return null;
        return userRepository.findByMail(mail).get();
    }

    public Object getAllUsers() {
        List<User> list = userRepository.findAll();
        List<UserDTO> listDTO = new ArrayList<>();

        if(list.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Error("No user."));

        for (User user: list) {
            listDTO.add(modelMapper.map(user, UserDTO.class));
        }

        return listDTO;
    }

    public String generateCommonLangPassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public ResponseEntity<?> createUser(UserRegistrationDTO userRegistrationDTO) {
        // Tests before inserting the data in db
        if(!(userRepository.findAllByMail(userRegistrationDTO.getEmail())).isEmpty())
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new Error("User already exists"));

        String password = this.generateCommonLangPassword();
        User newUser = new User();
        newUser.setCreation(new Date());
        newUser.setMail(userRegistrationDTO.getEmail());
        newUser.setName(userRegistrationDTO.getName() + " " + userRegistrationDTO.getSurname());
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setPhone_number(userRegistrationDTO.getPhone());
        newUser.setSociety(userRegistrationDTO.getSociety());
        newUser.setStatus("enabled");
        newUser.setIs_admin("false");

        userRepository.save(newUser);

        sendMail(userRegistrationDTO.getEmail(),"creation_compte", "Votre compte pour notre application de chat a été créé. \n" +
                "Voici votre mot de passe: " + password);

        return ResponseEntity.ok(new RegistrationConfirmationDTO("Registration success. Go to your emails to get your password.", password));
    }

    public ResponseEntity<?> createJourney(Long userId, Long formId, CreateJourneyDTO createJourneyDTO) {
        Journey newJourney = new Journey();
        Optional<User> user = userRepository.findById(userId);
        Optional<Form> form = formRepository.findById(formId);

        if(user.isEmpty() || !user.get().getStatus().equals("enabled"))
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Error("User does not exist or is disabled."));
        if(form.isEmpty() || !form.get().getStatus().equals("enabled"))
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Error("Form does not exist or is disabled."));
        if(journeyRepository.findByUserAndForm(user.get(), form.get()).isPresent())
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new Error("Journey already exists"));

        // Inserting data of form
        float score = 0;
        int questions_amnt = 0;

        newJourney.setDuration(createJourneyDTO.getDuration());
        newJourney.setForm(form.get());
        newJourney.setIntern(user.get());

        List<Answer> answerList = new ArrayList<>();
        List<Question> questionList = new ArrayList<>();

        // Inserting answers of the user
        for (CoupleQuestionAnswerDTO coupleQuestionAnswer:createJourneyDTO.getCoupleQuestionAnswerDTOList()) {
            // Get the good answer, and compare to the one picked
            Optional<Question> question = questionRepository.findById(coupleQuestionAnswer.getQuestionId());
            Optional<Answer> answer = answerRepository.findById(coupleQuestionAnswer.getAnswerId());

            if(question.isEmpty() || !Objects.equals(question.get().getStatus(), "enabled")
                    || answer.isEmpty() || !Objects.equals(answer.get().getStatus(), "enabled")) continue;

            // Update the score
            if(Objects.equals(question.get().getGood_answer().getId(), coupleQuestionAnswer.getAnswerId())) score++;
            questions_amnt++;

            // Prepare the answer to be saved
            answerList.add(answer.get());
            questionList.add(question.get());
        }

        newJourney.setScore(score/questions_amnt);

        // Saving the journey
        journeyRepository.save(newJourney);

        Journey journey = journeyRepository.findByUserAndForm(user.get(), form.get()).get();

        // Saving the answers of the user
        Iterator<Answer> answerIterator = answerList.iterator();
        Iterator<Question> questionIterator = questionList.iterator();

        while (answerIterator.hasNext() && questionIterator.hasNext()) {
            this.addJourneyAnswer(journey, questionIterator.next(), answerIterator.next());
        }

        return ResponseEntity.ok(new JourneyDTO(journey.getId(), journey.getScore(), journey.getDuration(), journey.getIntern().getId(), journey.getForm().getId()));
    }

    public ResponseEntity<?> updateJourney(JourneyUpdateDTO journeyDTO, Long userId, Long journeyId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Journey> journey = journeyRepository.findById(journeyId);

        if(user.isEmpty() || !user.get().getStatus().equals("enabled"))
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Error("User does not exist or is disabled."));
        if(journey.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Error("Journey not found."));
        if(!journey.get().getIntern().getId().equals(userId))
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Error("Journey and User do not match."));

        Journey journey1 = journey.get();
        journey1.setScore(journeyDTO.getScore());
        journey1.setDuration(journeyDTO.getDuration());

        journeyRepository.save(journey1);

        return ResponseEntity.ok(new JourneyDTO(journey1.getId(), journey1.getScore(), journey1.getDuration(), journey1.getIntern().getId(), journey1.getForm().getId()));
    }

    public ResponseEntity<?> updateUserActiveStatus(Long userId, String status) {
        Optional<User> optUser = userRepository.findById(userId);

        if(optUser.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Error("User does not exist."));

        User user = optUser.get();
        user.setStatus(status);
        userRepository.save(user);

        return ResponseEntity.ok(new UserEditionDTO(userId, status));
    }

    public Object getStatisticsOfUser(Long userId) {
        Optional<User> optUser = userRepository.findById(userId);

        StatisticsUserDTO statisticsUserDTO = new StatisticsUserDTO();

        if(optUser.isEmpty() || !optUser.get().getStatus().equals("enabled"))
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Error("User does not exist or is disabled."));

        User user = optUser.get();
        List<Journey> arrayJourney = journeyRepository.findAllByUser(user);

        if(arrayJourney.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Error("No journey for this user."));

        // Calculating the statistics
        float score = 0;
        float duration = 0;

        statisticsUserDTO.setJourneysAmount(arrayJourney.size());

        for (Journey journey : arrayJourney) {
            score += journey.getScore();
            duration += journey.getDuration();
            if(statisticsUserDTO.getBestScore() == null || statisticsUserDTO.getBestScore() < journey.getScore()) statisticsUserDTO.setBestScore(journey.getScore());
            if(statisticsUserDTO.getWorstScore() == null || statisticsUserDTO.getWorstScore() > journey.getScore()) statisticsUserDTO.setWorstScore(journey.getScore());
        }

        statisticsUserDTO.setAverageDuration((float) duration / arrayJourney.size());
        statisticsUserDTO.setAverageScore((float) score / arrayJourney.size());

        return statisticsUserDTO;
    }

    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(host);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    private void addJourneyAnswer(Journey journey, Question question, Answer answer){
        JourneyAnswer journeyAnswer = new JourneyAnswer();
        journeyAnswer.setJourney(journey);
        journeyAnswer.setQuestion(question);
        journeyAnswer.setAnswer(answer);

        journeyAnswerRepository.save(journeyAnswer);
    }

    public Object getJourneyData(Long journeyId) {
        //Getting the data of the journey
        Optional<Journey> optJourney = journeyRepository.findById(journeyId);

        if(optJourney.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Error("Journey not found."));

        Form form = optJourney.get().getForm();

        // Getting the data of the form
        List<JourneyEntryDataDTO> journeyEntryDataArray = new ArrayList<>();

        List<Question> questionList = this.questionRepository.findQuestionsByForm(form);

        if(questionList.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Error("No question found for this journey."));

        for (Question question: questionList) {
            // Loading the questions and answers
            List<Answer> answersList = this.answerRepository.findAnswersByQuestion(question);
            List<AnswerDTO> answersDTOList = new ArrayList<>();

            for (Answer answer: answersList) {
                answersDTOList.add(this.modelMapper.map(answer, AnswerDTO.class));
            }

            // Loading the answers entered by the user and the good answer
            Long answer_good = question.getGood_answer().getId();
            Optional<JourneyAnswer> optionalJourneyAnswer = journeyAnswerRepository.findByJourneyAndQuestion(optJourney.get(), question);
            Long answer_user = null;
            if(optionalJourneyAnswer.isPresent()) answer_user = optionalJourneyAnswer.get().getAnswer().getId();

            JourneyEntryDataDTO journeyEntryDataDTO = new JourneyEntryDataDTO(this.modelMapper.map(question, QuestionDTO.class), answersDTOList, answer_user, answer_good);
            journeyEntryDataArray.add(journeyEntryDataDTO);
        }

        // Adding the score and duration
        JourneyDataDTO journeyDataDTO = new JourneyDataDTO(journeyEntryDataArray, optJourney.get().getScore(), optJourney.get().getDuration());

        // Returning the result
        return journeyDataDTO;
    }
}
