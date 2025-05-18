package com.example.demo.Controllers;

import com.example.demo.DTO.*;
import com.example.demo.Model.ActivityLog;
import com.example.demo.Model.Budget;
import com.example.demo.Model.User;
import com.example.demo.BusinessLogic.UserService;
import com.example.demo.BusinessLogic.ProductService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ProductService productService;

    public UserController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserSignUpRequest request) {
        return userService.registerNewUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> request) {
        return userService.logout(request.get("username"));
    }

    @GetMapping("/activity-logs")
    public ResponseEntity<List<ActivityLog>> getActivityLog() {
        return ResponseEntity.ok(userService.getAllActivities());
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return userService.changePassword(request);
    }

    @GetMapping("/{username}/budgets")
    public ResponseEntity<?> getBudgetsByUsername(@PathVariable String username) {
        return userService.getBudgetsByUsernameResponse(username);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserSummaryDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsersWithoutPasswords());
    }


    @PostMapping("/{username}/upload-profile-picture")
    public ResponseEntity<?> uploadProfilePicture(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        return userService.saveProfilePicture(username, file);
    }

    @GetMapping("/profile-picture/{filename:.+}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String filename) {
        return userService.loadProfilePicture(filename);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@Valid @RequestBody TransferRequest request) {
        System.out.println("FROM: " + request.getFromCard());
        System.out.println("TO: " + request.getToCard());
        System.out.println("AMOUNT: " + request.getAmount());

        return userService.transfer(request);
    }



    @GetMapping("/{username}/export-history")
    public ResponseEntity<Resource> exportUserHistoryAsXml(@PathVariable String username) {
        try {
            File xmlFile = productService.generateXmlForUser(username);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(xmlFile));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=history.xml")
                    .contentType(MediaType.APPLICATION_XML)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
    @GetMapping("/{username}/monthly-expenses")
    public ResponseEntity<List<MonthlyExpenseDTO>> getMonthlyExpenses(@PathVariable String username) {
        try {
            List<MonthlyExpenseDTO> data = productService.getMonthlyExpensesForUser(username);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
