package camel.springboot.msg.adapter.data;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.hibernate.validator.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgA implements Serializable {

//  @NotBlank
  private String msg;
//  @NotBlank
//  @Pattern(regexp = "ru", message = "Only 'ru'")
  private String lng; // enum: [ru, en, es]
  private Coordinates coordinates;

}
