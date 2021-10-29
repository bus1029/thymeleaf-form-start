package hello.thymeleafformstart

import hello.thymeleafformstart.domain.item.Item
import hello.thymeleafformstart.domain.item.ItemRepository
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class TestDataInit constructor(private val itemRepository: ItemRepository) {
  /**
   * For test
   */
  @PostConstruct
  fun init() {
    itemRepository.save(Item("itemA", 10000, 10))
    itemRepository.save(Item("itemB", 20000, 20))
  }
}