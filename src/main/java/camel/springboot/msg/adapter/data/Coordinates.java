package camel.springboot.msg.adapter.data;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates implements Serializable {

//  @NotBlank
  double latitude; // Широта
//  @NotBlank
  double longitude; // Долгота

}
