package com.utc.formut.web.controllers;

import com.utc.formut.configuration.errors.Error;
import com.utc.formut.web.dto.*;
import com.utc.formut.web.models.Answer;
import com.utc.formut.web.models.Form;
import com.utc.formut.web.services.FormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Form")
@RestController
@RequestMapping("/form")
@Component
public class FormController {

    @Autowired
    private final FormService formService;

    public FormController(FormService formService){
        this.formService = formService;
    }

    ModelMapper modelMapper = new ModelMapper();


    @ApiResponse(
            content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FormDTO.class))) // ici mettre l'objet de sortie de la route
            })

    @Operation(summary = "Get all forms", description = "Retrieve all forms' metadata")
    @GetMapping(value  = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllForms(){
        return this.formService.getAllForms();
    }

    @Operation(summary = "Get a form", description = "Retrieve a form's metadata")
    @GetMapping(value  = "/{form_id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public FormDTO getForm(@PathVariable("form_id") Long form_id) throws Exception {
        return modelMapper.map(this.formService.getForm(form_id), FormDTO.class);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = FormDataDTO.class
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
    @Operation(summary = "Get the questions and possible answers of a form", description = "Retrieve data of a form")
    @GetMapping(value  = "/{form_id}/content",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getDataOfAForm(@PathVariable("form_id") Long form_id){
        return this.formService.getDataOfAForm(form_id);
    }

    @Operation(summary = "Create form", description = "Create a form from a DTO with the right parameters")
    @PostMapping(value  = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public FormDTO createForm(@RequestBody @NotBlank FormCreationDTO formCreationDTO){
        return this.formService.createForm(formCreationDTO);
    }

    @Operation(summary = "update form", description = "update form (question order, question status)")
    @PutMapping(value  = "/{form_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateForm(@RequestBody @NotBlank FormDTO formDTO, @PathVariable("form_id") Long id) throws Exception {

        this.formService.updateForm(formDTO, id);

    }

    @Operation(summary = "delete form", description = "delete form")
    @DeleteMapping(value  = "/{form_id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteForm(@PathVariable("form_id") Long id) throws Exception {
        this.formService.deleteForm(id);
    }

    /*
    STATS :
    - For a form : average/best note, number of journeys
    - For a subject : average/best note, number of journeys
     */
    @Operation(summary = "Get stats from a form", description = "Views statistics about a given form (average, max, number of journeys, ...)")
    @GetMapping(value  = "/{form_id}/stats",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public FormStatsDTO getFormStatistic(@PathVariable("form_id") Long id){
        // TODO :
        return new FormStatsDTO();
    }

    @Operation(summary = "Get stats from all forms in a subject", description = "Views statistics about a given subject (average, max, number of journeys, number of forms...)")
    @GetMapping(value  = "/subject/{subject_name}/stats",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public SubjectStatsDTO getSubjectStatistic(@PathVariable("subject_name") String subject_name){
        // TODO :
        return new SubjectStatsDTO();
    }





}
