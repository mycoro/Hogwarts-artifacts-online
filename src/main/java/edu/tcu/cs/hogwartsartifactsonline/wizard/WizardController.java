package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.wizard.converter.WizardDtoToWizardConverter;
import edu.tcu.cs.hogwartsartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {

    private final WizardService wizardService;

    private final WizardDtoToWizardConverter wizardDtoToWizardConverter;

    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    public WizardController(WizardService wizardService, WizardDtoToWizardConverter wizardDtoToWizardConverter, WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardService = wizardService;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }

    @GetMapping
    public Result findAllWizards() {
        List<Wizard> foundWizards = this.wizardService.findAll();

        List<WizardDto> wizardDtos = foundWizards.stream().map(this.wizardToWizardDtoConverter::convert).collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", wizardDtos);
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId) {
        Wizard foundWizard = this.wizardService.findById(wizardId);
        WizardDto wizardDto = this.wizardToWizardDtoConverter.convert(foundWizard);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", wizardDto);
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto) {
        Wizard newWizard = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard savedWizard = this.wizardService.save(newWizard);
        WizardDto savedWizardDto = this.wizardToWizardDtoConverter.convert(savedWizard);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedWizardDto);
    }

    @PutMapping("/{wizardId}")
    public Result updateWizard(@PathVariable Integer wizardId, @Valid @RequestBody WizardDto wizardDto) {
        Wizard update = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard updateWizard = this.wizardService.update(wizardId, update);
        WizardDto updateWizardDto = this.wizardToWizardDtoConverter.convert(updateWizard);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updateWizardDto);
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId) {
        this.wizardService.delete(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

    @PutMapping("/{wizardId}/artifacts/{artifactId}")
    public Result assignArtifact(@PathVariable Integer wizardId, @PathVariable String artifactId) {
        this.wizardService.assignArtifact(wizardId, artifactId);
        return new Result(true, StatusCode.SUCCESS, "Artifact Assignment Success");
    }
}
