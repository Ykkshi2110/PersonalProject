package vn.peterbui.myproject.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {
    private int page; // page: Trang hiện tại
    private int pageSize; // limit: Số lượng bản ghi đã lấy 
    private int pages; // totalPages: Tổng số trang với điều kiện query
    private long total; // totalItems: Tổng số phần tử (tổng số bản ghi)
}
