package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.account.staff.model.request.*;
import com.example.shoestore.core.account.staff.service.AdminAccountService;
import com.example.shoestore.core.common.dto.response.ResponseFactory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@CrossOrigin("*")
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private AdminAccountService adminAccountService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> findAdmin(@RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "10") int size,
                                            @RequestParam(name = "sortField", required = false) String sortField,
                                            @RequestParam(name = "sortOrder", required = false) String sortOrder,
                                            @RequestParam(name = "fullname", required = false) String fullname,
                                            @RequestParam(name = "email", required = false) String email,
                                            @RequestParam(name = "phonenumber", required = false) String phonenumber,
                                            @RequestParam(name = "gender", required = false) Boolean gender,
                                            @RequestParam(name = "createdTime", required = false) LocalDateTime createdTime,
                                            @RequestParam(name = "updatedTime", required = false) LocalDateTime updatedTime) {
        return ResponseFactory.data(adminAccountService.adminPage(fullname, email, phonenumber, gender, createdTime, updatedTime, page, size, sortField, sortOrder));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createAdmin(@Valid @RequestBody CreateAdminDTO accountDTO) {
        return ResponseFactory.data(adminAccountService.createAdmin(accountDTO));
    }

    @PostMapping("/create-admin")
    public ResponseEntity<Object> createSuperAdmin() {
        return ResponseFactory.data(adminAccountService.createSuperAdmin());
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> updateAdmin(@Valid @RequestBody UpdateAccountDTO updateAccountDTO) {
        return ResponseFactory.data(adminAccountService.updateAdmin(updateAccountDTO));
    }

    @PatchMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteAdmin(@RequestBody DeleteAccountDTO deleteAccountDTO) {
        return ResponseFactory.data(adminAccountService.deleteAdmin(deleteAccountDTO));
    }


    @GetMapping("/detail/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Object> detailAdmin(@PathVariable("id") Long id) {
        return ResponseFactory.data(adminAccountService.findAdmin(id));
    }


    @GetMapping("/sms")
    public ResponseEntity<Object> sms(@RequestParam(name = "phonenumberOrEmail") String phonenumberOrEmail) {
        return ResponseFactory.data(adminAccountService.OTPSend(phonenumberOrEmail));
    }

    @GetMapping("/otpcheck")
    public ResponseEntity<Object> check(@RequestParam(name = "otp") int otp) {
        return ResponseFactory.data(adminAccountService.OTPCheck(otp));
    }


    @PutMapping("/forgotPassword")
    public ResponseEntity<Object> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        return ResponseFactory.data(adminAccountService.forgotPassword(forgotPasswordDTO));
    }

    @PutMapping("/changePassword")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        return ResponseFactory.data(adminAccountService.changePassword(changePasswordDTO));
    }


    @PutMapping("/{id}/multipart-file")
    public ResponseEntity<Object> changeAvatar(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        return ResponseFactory.data(adminAccountService.changeAvatar(id, file));
    }

    @PutMapping("/update-status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        return ResponseFactory.data(adminAccountService.updateStatus(status, id));
    }

}
