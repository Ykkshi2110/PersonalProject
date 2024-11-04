package vn.peterbui.myproject.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO{
    private Meta meta;
    private Object result;
}
