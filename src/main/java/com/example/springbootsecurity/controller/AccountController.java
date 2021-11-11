package com.example.springbootsecurity.controller;

import com.example.springbootsecurity.domain.Account;
import com.example.springbootsecurity.dto.AccountFormDTO;
import com.example.springbootsecurity.service.account.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountServiceImpl accountService;

//    @RequestMapping(method = RequestMethod.POST, path = "/account")
    @PostMapping("")
    public ResponseEntity<?> insertAccount(
            @Valid @RequestBody AccountFormDTO dto,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Account accountDB = accountService.saveOrUpdateAccount(dto.toEntity());
        return new ResponseEntity<>(accountDB, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> viewAccount() {
        return new ResponseEntity<>("Success!", HttpStatus.OK);
    }

}
