package com.sxzy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class SteelQueryResult {

    String steel_P_Id;

    Steel steel;
}
