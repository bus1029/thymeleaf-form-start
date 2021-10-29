package hello.thymeleafformstart.domain.item

import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class ItemRepository {
  companion object {
    val store = ConcurrentHashMap<Long, Item>()
    var sequence = AtomicLong()
  }

  fun save(item: Item): Item {
    item.id = sequence.addAndGet(1)
    store[item.id] = item
    return item
  }

  fun findById(id: Long): Item? {
    return store[id]
  }

  fun findAll(): List<Item> {
    return ArrayList<Item>(store.values)
  }

  fun update(itemId: Long, updateParam: Item) {
    val findItem = findById(itemId)
    // 수신 객체 람다 내부에서 수신 객체의 함수를 사용하지 않고 수신 객체 자신을 다시 반환 하려는 경우에 apply 를 사용
    findItem?.apply {
      findItem.itemName = updateParam.itemName
      findItem.price = updateParam.price
      findItem.quantity = updateParam.quantity
      findItem.open = updateParam.open
      findItem.regions = updateParam.regions
      findItem.itemType = updateParam.itemType
      findItem.deliveryCode = updateParam.deliveryCode
    }
  }

  fun clearStore() {
    store.clear()
  }
}