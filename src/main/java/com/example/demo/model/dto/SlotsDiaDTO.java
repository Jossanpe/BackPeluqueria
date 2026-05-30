package com.example.demo.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SlotsDiaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

    private LocalDate fecha;

    private List<LocalTime> slots;
}