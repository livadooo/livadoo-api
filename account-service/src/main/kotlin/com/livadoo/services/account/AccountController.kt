import com.livadoo.services.account.AccountService
import com.livadoo.services.account.PasswordReset
import com.livadoo.services.account.PasswordResetRequest
import com.livadoo.utils.user.UserDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/accounts")
class AccountController(
    private val accountService: AccountService,
) {
    @GetMapping
    suspend fun getCurrentUser(): UserDto {
        return accountService.getCurrentUser()
    }

    @PostMapping("/request-password-reset")
    suspend fun requestPasswordReset(
        @RequestBody passwordResetRequest: PasswordResetRequest,
    ) {
        return accountService.requestPasswordReset(passwordResetRequest)
    }

    @PostMapping("/reset-password")
    suspend fun resetPassword(
        @RequestBody passwordReset: PasswordReset,
    ) {
        return accountService.resetPassword(passwordReset)
    }
}
