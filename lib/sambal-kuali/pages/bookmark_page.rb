class BookmarkPage < BasePage

  page_url "#{$test_site}kr-krad/lookup"

  wrapper_elements
  frame_element
  expected_element :browser_secondary_nav


  #10-Browser sec navigation

  element(:browser_secondary_nav) { |b| b.a(id: "Ksap-Header-Bookmark-Count")}
  element(:bookmark_page) { |b|b.main(id:"bookmark_detail_page")}
  element(:bookmark_details) { |b|b.div(id:"bookmark_detail")}


 end

