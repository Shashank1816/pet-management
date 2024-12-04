```
package org.springframework.samples.petclinic.owner;

import java.util.List;
import java.util.Map;

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

	private static final String OWNER_FORM_VIEW = "owners/createOrUpdateOwnerForm";
	private final OwnerRepository ownerRepository;

	public OwnerController(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable(name = "ownerId", required = false) Integer ownerId) {
		return ownerId == null ? new Owner() : ownerRepository.findById(ownerId);
	}

	@GetMapping("/owners/new")
	public String initCreationForm(Map<String, Object> model) {
		model.put("owner", new Owner());
		return OWNER_FORM_VIEW;
	}

	@PostMapping("/owners/new")
	public String processCreationForm(@Valid Owner owner, BindingResult result) {
		if (result.hasErrors()) {
			return OWNER_FORM_VIEW;
		}
		ownerRepository.save(owner);
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

		Page<Owner> ownersResults = findPaginatedByLastName(page, owner.getLastName());
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
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listOwners", paginated.getContent());
		return "owners/ownersList";
	}

	private Page<Owner> findPaginatedByLastName(int page, String lastName) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return ownerRepository.findByLastName(lastName, pageable);
	}

	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
		model.addAttribute(ownerRepository.findById(ownerId));
		return OWNER_FORM_VIEW;
	}

	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result,
			@PathVariable("ownerId") int ownerId) {
		if (result.hasErrors()) {
			return OWNER_FORM_VIEW;
		}
		owner.setId(ownerId);
		ownerRepository.save(owner);
		return "redirect:/owners/{ownerId}";
	}

	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		mav.addObject(ownerRepository.findById(ownerId));
		return mav;
	}
}
```

In this refactored code, I made the following changes to enhance readability and maintainability:

1. **Renamed Constants**: Changed `VIEWS_OWNER_CREATE_OR_UPDATE_FORM` to `OWNER_FORM_VIEW` for clarity and conciseness.
2. **Renamed Variables**: Changed `owners` to `ownerRepository` to better reflect its purpose as a repository for owner entities.
3. **Method Naming**: Renamed `findPaginatedForOwnersLastName` to `findPaginatedByLastName` for better clarity and consistency in naming conventions.
4. **Removed Redundant Code**: Simplified the creation of a new `Owner` object in `initCreationForm` by directly putting it into the model.
5. **Consistent Formatting**: Ensured consistent formatting and spacing throughout the code for better readability.
6. **Streamlined Logic**: Removed unnecessary comments that were self-explanatory and improved the overall flow of the code.

These changes aim to improve the overall structure and clarity of the code without altering its functionality.