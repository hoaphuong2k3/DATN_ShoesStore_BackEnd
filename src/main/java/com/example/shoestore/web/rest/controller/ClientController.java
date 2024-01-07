package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.account.client.model.request.RegisterClientDTO;
import com.example.shoestore.core.account.client.model.request.UpdateClientDTO;
import com.example.shoestore.core.account.client.service.ClientService;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import com.example.shoestore.core.account.staff.model.request.ChangePasswordDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> findAccount(@PathVariable("id") Long id) {
        return ResponseFactory.data(clientService.findAccount(id));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> sigin(@Valid @RequestBody RegisterClientDTO clientDTO) {
        return ResponseFactory.data(clientService.createAccount(clientDTO));
    }

    @PatchMapping("/update")
    public ResponseEntity<Object> update(@RequestBody UpdateClientDTO clientDTO) {
        return ResponseFactory.data(clientService.updateAccount(clientDTO));
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        return ResponseFactory.data(clientService.deleteAccount(id));
    }

    @GetMapping("/sms")
    public ResponseEntity<Object> sms(@RequestParam(name = "phonenumberOrEmail") String phonenumberOrEmail) {
        return ResponseFactory.data(clientService.OTPSend(phonenumberOrEmail));
    }

    @GetMapping("/otpcheck")
    public ResponseEntity<Object> check(@RequestParam(name = "otp") int otp) {
        return ResponseFactory.data(clientService.OTPCheck(otp));
    }


    @PutMapping("/changePassword")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        return ResponseFactory.data(clientService.changePassword(changePasswordDTO));
    }

    @PutMapping("/{id}/multipart-file")
    public ResponseEntity<Object> changeAvatar(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        return ResponseFactory.data(clientService.changeAvatar(id, file));
    }
}
