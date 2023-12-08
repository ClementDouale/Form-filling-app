package com.utc.formut.web.controllers;

import com.utc.formut.configuration.errors.Error;
import com.utc.formut.configuration.security.AuthenticationService;
import com.utc.formut.configuration.security.JwtRequestModel;
import com.utc.formut.configuration.security.JwtResponseModel;
import com.utc.formut.configuration.security.TokenManager;
import com.utc.formut.web.dto.*;
import com.utc.formut.web.models.Form;
import com.utc.formut.web.models.Journey;
import com.utc.formut.web.models.User;
import com.utc.formut.web.repositories.UserRepository;
import com.utc.formut.web.services.FormService;
import com.utc.formut.web.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FormService formService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationService userDetailsService;

    @Autowired
    private TokenManager tokenManager;


    // ROUTE LOGIN
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = JwtResponseModel.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            )
    })
    @Operation(summary = "Login a user", description = "login a user ( admin and intern )")
    @PostMapping(value = "/user/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponseModel> createToken(@RequestBody JwtRequestModel request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwtToken = tokenManager.generateJwtToken(userDetails);

        return ResponseEntity.ok(new JwtResponseModel(jwtToken, userService.getUserByMail(request.getUsername()).getId()));
    }

    // ROUTE REGISTER
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RegistrationConfirmationDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            )
    })
    @Operation(summary = "register a user", description = "register a user")
    @PostMapping(value = "/user/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody @NotBlank UserRegistrationDTO userRegistrationDTO){
        return userService.createUser(userRegistrationDTO);
    }

    // ROUTE START A JOURNEY
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = JourneyDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            )
    })
    @Operation(summary = "Start a journey", description = "intern starting a journey")
    @PostMapping(value = "/user/{user_id}/form/{form_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createJourney(@PathVariable("user_id") Long user_id, @PathVariable("form_id") Long form_id, @RequestBody CreateJourneyDTO createJourneyDTO){
        return userService.createJourney(user_id, form_id, createJourneyDTO);
    }

    // ROUTE UPDATE A JOURNEY
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = JourneyDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            )
    })
    @Operation(summary = "Update the journey", description = "Intern realizing the journey, can complete questions , stop, and come back later")
    @PutMapping(value = "/user/{user_id}/journey/{journey_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateJourney(@RequestBody JourneyUpdateDTO journeyUpdateDTO, @PathVariable("user_id") Long user_id, @PathVariable("journey_id") Long journey_id){
        return userService.updateJourney(journeyUpdateDTO, user_id, journey_id);
    }

    // ROUTE TO GET DETAILS OF A JOURNEY
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = JourneyDataDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            )
    })
    @Operation(summary = "Get the details of a journey", description = "Get journey data of a user")
    @GetMapping(value = "/journey/{journey_id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getJourneyData(@PathVariable("journey_id") Long id){
        return userService.getJourneyData(id);
    }

    // ROUTE TO EDIT THE ACTIVITY OF A USER
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = UserEditionDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            )
    })
    @Operation(summary = "edit user activity", description = "edit user activity ( status )")
    @PutMapping(value = "/user/{user_id}/status/{status}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUserActiveStatus(@PathVariable("user_id") Long user_id, @PathVariable("status") String status){
        return userService.updateUserActiveStatus(user_id, status);
    }

    // ROUTE GET PROFILE USER
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = UserDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            )
    })
    @Operation(summary = "get user", description = "get user")
    @GetMapping(value = "/user/{user_id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable("user_id") Long id){
        return userService.getUser(id);
    }

    // ROUTE GET ALL JOURNEY OF USER
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ListJourneysDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            )
    })
    @Operation(summary = "get all journey of a user", description = "get all journey of a user")
    @GetMapping(value = "/user/{user_id}/journey",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getAllJourneysOfUser(@PathVariable("user_id") Long id){
        return userService.getJourneysOfUser(id);
    }

    // ROUTE GET ALL JOURNEYS (ADMIN ONLY)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ListJourneysDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            )
    })
    @Operation(summary = "get all journey", description = "get all journeys")
    @GetMapping(value = "/user/journey/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getAllJourneys(){
        return userService.getAllJourneys();
    }

    // GET ALL USERS (ADMIN ONLY)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ListUsersDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            )
    })
    @Operation(summary = "get all users", description = "get all users")
    @GetMapping(value = "/user/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getAllUsers(){
        return userService.getAllUsers();
    }

    // GET STATISTICS OF USER ON ALL JOURNEYS
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = StatisticsUserDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            )
    })
    @Operation(summary = "get user Statistics on a form", description = "get statistics of a User (intern) on a form")
    @GetMapping(value = "/user/{user_id}/statistics",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getUserStatistics(@PathVariable("user_id") Long user_id){
        return userService.getStatisticsOfUser(user_id);
    }
}
