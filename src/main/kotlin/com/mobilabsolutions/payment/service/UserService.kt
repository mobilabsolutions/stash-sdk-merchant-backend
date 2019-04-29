package com.mobilabsolutions.payment.service

import com.mobilabsolutions.payment.common.util.RandomStringGenerator
import com.mobilabsolutions.payment.data.domain.User
import com.mobilabsolutions.payment.data.repository.UserRepository
import com.mobilabsolutions.payment.model.response.CreateUserResponseModel
import mu.KLogging
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * @author <a href="mailto:jovana@mobilabsolutions.com">Jovana Veskovic</a>
 */
@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val randomStringGenerator: RandomStringGenerator
) {
    companion object : KLogging() {
        const val USER_ID_LENGTH = 16
    }

    /**
     * Creating test user with only id.
     *
     * @return Create User Response
     */
    fun createUser(): CreateUserResponseModel {
        logger.info { "Creating user" }
        val userId = randomStringGenerator.generateRandomAlphanumeric(USER_ID_LENGTH)
        userRepository.save(User(id = userId))
        return CreateUserResponseModel(userId)
    }
}
