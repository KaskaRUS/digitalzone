package space.zhdanov.testwork.digitalzone.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ResponseAggregationEvents {

    private Integer amountAllVisits;
    private Integer amountUniqueUser;
    private Integer amountRegularUser;
}
