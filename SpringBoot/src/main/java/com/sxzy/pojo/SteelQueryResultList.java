package com.sxzy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class SteelQueryResultList {
    List<SteelQueryResult> steels;

    public List<SteelQueryResult> getSteels() {
        return steels;
    }
}
