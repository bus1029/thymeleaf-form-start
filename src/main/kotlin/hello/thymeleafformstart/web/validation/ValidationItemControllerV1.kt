package hello.thymeleafformstart.web.validation

import hello.thymeleafformstart.domain.item.Item
import hello.thymeleafformstart.domain.item.ItemRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/validation/v1/items")
class ValidationItemControllerV1 constructor(private val itemRepository: ItemRepository) {
  @GetMapping
  fun items(model: Model): String {
    val items = itemRepository.findAll()
    model.addAttribute("items", items)
    return "validation/v1/items"
  }

  @GetMapping("/{itemId}")
  fun item(@PathVariable itemId: Long, model: Model): String {
    val item = itemRepository.findById(itemId)
    model.addAttribute("item", item)
    return "validation/v1/item"
  }

  @GetMapping("/add")
  fun addForm(model: Model): String {
    model.addAttribute("item", Item())
    return "validation/v1/addForm"
  }

  @PostMapping("/add")
  fun addItem(@ModelAttribute item: Item, redirectAttributes: RedirectAttributes): String {
    val savedItem = itemRepository.save(item)
    redirectAttributes.addAttribute("itemId", savedItem.id)
    redirectAttributes.addAttribute("status", true)
    return "redirect:/validation/v1/items/{itemId}"
  }

  @GetMapping("/{itemId}/edit")
  fun editForm(@PathVariable itemId: Long, model: Model): String {
    val item = itemRepository.findById(itemId)
    model.addAttribute("item", item)
    return "validation/v1/editForm"
  }

  @PostMapping("/{itemId}/edit")
  fun edit(@PathVariable itemId: Long, @ModelAttribute item: Item): String {
    itemRepository.update(itemId, item)
    return "redirect:/validation/v1/items/{itemId}"
  }
}