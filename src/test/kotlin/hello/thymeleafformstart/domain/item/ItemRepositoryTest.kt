package hello.thymeleafformstart.domain.item

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class ItemRepositoryTest {
  private val itemRepository = ItemRepository()

  @AfterEach
  fun afterEach() {
    itemRepository.clearStore()
  }

  @Test
  fun save() {
    val item = Item("ItemA", 10000, 10)
    val savedItem = itemRepository.save(item)
    val findById = itemRepository.findById(item.id)
    assertThat(findById).isEqualTo(savedItem)
  }

  @Test
  fun findAll() {
    val item1 = Item("ItemA", 10000, 10)
    val item2 = Item("ItemB", 10000, 10)
    itemRepository.save(item1)
    itemRepository.save(item2)

    val findItems = itemRepository.findAll()
    assertThat(findItems.size).isEqualTo(2)
    assertThat(findItems).contains(item1, item2)
  }

  @Test
  fun updateItem() {
    val item1 = Item("ItemA", 10000, 10)
    itemRepository.save(item1)
    val item2 = Item("ItemB", 100000, 100)
    itemRepository.update(item1.id, item2)

    val findById = itemRepository.findById(item1.id)
    assertThat(findById).isEqualTo(item2)
  }
}