package hello.thymeleafformstart.domain.item

enum class ItemType constructor(private val description: String = ""){
  BOOK("도서"), FOOD("음식"), ETC("기타")

}