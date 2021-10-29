package hello.thymeleafformstart.web.form

import hello.thymeleafformstart.domain.item.Item
import hello.thymeleafformstart.domain.item.ItemRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/form/items")
class FormItemController @Autowired constructor (private val itemRepository: ItemRepository) {
  var logger: Logger = LoggerFactory.getLogger(FormItemController::class.java)

  @GetMapping
  fun items(model: Model): String {
    val items = itemRepository.findAll()
    model.addAttribute("items", items)
    return "form/items"
  }

  @GetMapping("/{itemId}")
  fun item(@PathVariable itemId: Long, model: Model): String {
    val item = itemRepository.findById(itemId)
    model.addAttribute("item", item)
    return "form/item"
  }

  @GetMapping("/add")
  fun addForm(model: Model): String {
    model.addAttribute("item", Item())
    return "form/addForm"
  }

  @PostMapping("/add")
  fun addItem(@ModelAttribute item: Item, redirectAttribute: RedirectAttributes): String {
    // Item (class name) -> item
    logger.info("item.open={}", item.open)

    val savedItem = itemRepository.save(item)
    redirectAttribute.addAttribute("itemId", savedItem.id)
    // 나머지 attribute 들은 query parameter 형식으로 들어감
    redirectAttribute.addAttribute("status", true)
    return "redirect:/form/items/{itemId}"
  }

  @GetMapping("/{itemId}/edit")
  fun editItem(@PathVariable itemId: Long, model: Model): String {
    val findById = itemRepository.findById(itemId)
    model.addAttribute("item", findById)
    return "form/editForm"
  }

  @PostMapping("/{itemId}/edit")
  fun edit(@PathVariable itemId: Long, @ModelAttribute item: Item): String {
    itemRepository.update(itemId, item)
    return "redirect:/form/items/{itemId}"
  }
}