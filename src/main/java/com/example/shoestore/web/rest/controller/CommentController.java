package com.example.shoestore.web.rest.controller;

import com.example.shoestore.core.feedback.dto.request.CreateCommentRequest;
import com.example.shoestore.core.feedback.dto.request.UpdateCommentRequest;
import com.example.shoestore.core.feedback.service.UserCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/comment")
public class CommentController {

    @Autowired
    UserCommentService userCommentService;

    @GetMapping("/getAll/{id}")
    public ResponseEntity<Object> getAll(@PathVariable("id") Long id){
        return ResponseEntity.ok(userCommentService.getAllComment(id));
    }

    @GetMapping("/getAll-account/{id}")
    public ResponseEntity<Object> getAllByAccount(@PathVariable("id") Long id){
        return ResponseEntity.ok(userCommentService.getAllCommentByAccount(id));
    }

    @GetMapping("/getAll-shoe/{id}")
    public ResponseEntity<Object> getAllByAccountId(@PathVariable("id") Long id){
        return ResponseEntity.ok(userCommentService.getAllProductByAccount(id));
    }

    @PostMapping("/createComment")
    public ResponseEntity<Object> create(@RequestBody CreateCommentRequest createComment){
        return ResponseEntity.ok(userCommentService.createComment(createComment));
    }

    @PutMapping("/updateComment")
    public ResponseEntity<Object> update(@RequestBody UpdateCommentRequest updateComment){
        return ResponseEntity.ok(userCommentService.updateComment(updateComment));
    }

    @DeleteMapping("/deleteComment/{id}")
    public ResponseEntity<Object> delete(@RequestBody Long id){
        return ResponseEntity.ok(userCommentService.deleteComment(id));
    }
}
