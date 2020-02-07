package poi.excel.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Albumen
 * @date 2020/1/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Head {
    @ExcelProperty("第一列")
    private String firstLine;
    @ExcelProperty("第二列")
    private String secondLine;
    @ExcelProperty("第三列")
    private String thirdLine;
}
