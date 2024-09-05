package vn.peterbui.myproject.domain.dto;

import lombok.Getter;
import lombok.Setter;
import vn.peterbui.myproject.domain.Meta;

@Getter
@Setter
public class ResultPaginationDTO{
    private Meta meta;
    private Object result;
}
