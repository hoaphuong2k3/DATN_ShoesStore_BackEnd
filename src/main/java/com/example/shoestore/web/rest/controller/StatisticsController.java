package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin("*")
@RequestMapping("/statistics")
@PreAuthorize("hasRole('ADMIN')")
public class StatisticsController {


    @Autowired
    StatisticsService statisticsService;

    @GetMapping("/quantity")
    public ResponseEntity<Object> getQuantity(@RequestParam(value = "fromDate", required = false) LocalDate fromDate,
                                              @RequestParam(value = "toDate", required = false) LocalDate toDate){
        return ResponseFactory.data(statisticsService.getAllQuantity(fromDate, toDate));
    }

    @GetMapping("/week")
    public ResponseEntity<Object> getWeek(){
        return ResponseFactory.data(statisticsService.getAllPerWeek());
    }

    @GetMapping("/week-before")
    public ResponseEntity<Object> getPercentWeek(){
        return ResponseFactory.data(statisticsService.getAllPerWeekBefore());
    }

    @GetMapping("/month")
    public ResponseEntity<Object> getMonth(){
        return ResponseFactory.data(statisticsService.getAllPerMonth());
    }

    @GetMapping("/month-before")
    public ResponseEntity<Object> getPercentMonth(){
        return ResponseFactory.data(statisticsService.getAllPerMonthBefore());
    }

    @GetMapping("/year")
    public ResponseEntity<Object> getYear(){
        return ResponseFactory.data(statisticsService.getAllPerYear());
    }

    @GetMapping("/year-before")
    public ResponseEntity<Object> getPercentYear(){
        return ResponseFactory.data(statisticsService.getAllPerYearBefore());
    }

    @GetMapping("/client")
    public ResponseEntity<Object> getClient(){
        return ResponseFactory.data(statisticsService.getAllClientSignUp());
    }

    @GetMapping("/top-5-product")
    public ResponseEntity<Object> getTopProduct(@RequestParam(value = "fromDate", required = false) LocalDate fromDate,
                                                @RequestParam(value = "toDate", required = false) LocalDate toDate) {
        return ResponseFactory.data(statisticsService.getAllTopProduct(fromDate,toDate));
    }

    @GetMapping("/bottom-5-product")
    public ResponseEntity<Object> getBottomProduct(@RequestParam(value = "fromDate", required = false) LocalDate fromDate,
                                                   @RequestParam(value = "toDate", required = false) LocalDate toDate) {
        return ResponseFactory.data(statisticsService.getAllBottomProduct(fromDate,toDate));
    }

    @GetMapping("/data-product")
    public ResponseEntity<Object> getDataPerMonth(@RequestParam(value = "month", required = false) Integer month,
                                                  @RequestParam(value = "year", required = false) Integer year) {
        return ResponseFactory.data(statisticsService.getDataPerMonth(month, year));
    }

    @GetMapping("/data-product-year")
    public ResponseEntity<Object> getDataPerYear(@RequestParam(value = "year", required = false) Integer year) {
        return ResponseFactory.data(statisticsService.getDataPerYear(year));
    }

    @GetMapping("/detail-product/{id}")
    public ResponseEntity<Object> getDetailShoe(@PathVariable("id") Long id) {
        return ResponseFactory.data(statisticsService.getDetailShoe(id));
    }

    @GetMapping("/data-payment")
    public ResponseEntity<Object> getDataPayment(@RequestParam(value = "saleStatus", required = false) Integer saleStatus,
                                                 @RequestParam(value = "sale", required = false) Integer sale) {
        return ResponseFactory.data(statisticsService.getTotalPayment(saleStatus, sale));
    }

}
