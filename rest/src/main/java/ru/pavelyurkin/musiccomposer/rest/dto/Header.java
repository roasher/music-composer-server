package ru.pavelyurkin.musiccomposer.rest.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by Wish on 11.07.2017.
 * <p>
 * "header": {
 * "tempo": 64,
 * "timeSignature": [
 * 	4,
 * 	4
 * ]
 * }
 */
@Data
public class Header {

	private double tempo;
	private List<Integer> timeSignature;

}