```
package org.springframework.samples.petclinic.owner;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

@Controller
class OwnerController {

    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
    private static final Logger logger = LoggerFactory.getLogger(OwnerController.class);

    private final OwnerRepository owners;

    public OwnerController(OwnerRepository clinicService) {
        this.owners = clinicService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable(name = "ownerId", required = false) Integer ownerId) {
        return ownerId == null ? new Owner() : this.owners.findById(ownerId);
    }

    @GetMapping("/owners/new")
    public String initCreationForm(Map<String, Object> model) {
        model.put("owner", new Owner());
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/owners/new")
    public String processCreationForm(@Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        }

        try {
            this.owners.save(owner);
        } catch (Exception e) {
            logger.error("Error saving owner: {}", e.getMessage());
            result.reject("saveError", "An error occurred while saving the owner.");
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        }

        return "redirect:/owners/" + owner.getId();
    }

    @GetMapping("/owners/find")
    public String initFindForm() {
        return "owners/findOwners";
    }

    @GetMapping("/owners")
    public String processFindForm(@RequestParam(defaultValue = "1") int page, Owner owner, BindingResult result,
                                   Model model) {
        if (owner.getLastName() == null) {
            owner.setLastName(""); // empty string signifies broadest possible search
        }

        Page<Owner> ownersResults;
        try {
            ownersResults = findPaginatedForOwnersLastName(page, owner.getLastName());
        } catch (Exception e) {
            logger.error("Error finding owners: {}", e.getMessage());
            result.reject("findError", "An error occurred while finding owners.");
            return "owners/findOwners";
        }

        if (ownersResults.isEmpty()) {
            result.rejectValue("lastName", "notFound", "not found");
            return "owners/findOwners";
        }

        if (ownersResults.getTotalElements() == 1) {
            owner = ownersResults.iterator().next();
            return "redirect:/owners/" + owner.getId();
        }

        return addPaginationModel(page, model, ownersResults);
    }

    private String addPaginationModel(int page, Model model, Page<Owner> paginated) {
        List<Owner> listOwners = paginated.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("listOwners", listOwners);
        return "owners/ownersList";
    }

    private Page<Owner> findPaginatedForOwnersLastName(int page, String lastname) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return owners.findByLastName(lastname, pageable);
    }

    @GetMapping("/owners/{ownerId}/edit")
    public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
        Owner owner = this.owners.findById(ownerId);
        model.addAttribute(owner);
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/owners/{ownerId}/edit")
    public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result,
                                          @PathVariable("ownerId") int ownerId) {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        }

        owner.setId(ownerId);
        try {
            this.owners.save(owner);
        } catch (Exception e) {
            logger.error("Error updating owner: {}", e.getMessage());
            result.reject("updateError", "An error occurred while updating the owner.");
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        }

        return "redirect:/owners/{ownerId}";
    }

    @GetMapping("/owners/{ownerId}")
    public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
        ModelAndView mav = new ModelAndView("owners/ownerDetails");
        Owner owner;
        try {
            owner = this.owners.findById(ownerId);
        } catch (Exception e) {
            logger.error("Error retrieving owner details: {}", e.getMessage());
            mav.addObject("error", "Owner not found.");
            return mav;
        }
        mav.addObject(owner);
        return mav;
    }
}
```

In this refactored code, I have made several changes to enhance error handling and logging:

1. **Logging**: Introduced SLF4J logging to capture errors during owner creation, updating, and retrieval. This allows for better tracking of issues without cluttering the main logic.

2. **Error Handling**: Added try-catch blocks around critical operations (like saving and finding owners) to handle exceptions gracefully. If an error occurs, a relevant message is logged, and the user is informed through the `BindingResult`.

3. **Code Cleanup**: Minor adjustments were made for readability, such as simplifying the creation of new `Owner` objects and ensuring consistent formatting.

These changes improve the robustness of the application by providing clear error messages and logging, which can be invaluable for debugging and maintaining the application.