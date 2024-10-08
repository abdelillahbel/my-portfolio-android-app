/*
 * Copyright (c) 2024. DevUnion Foundation.
 * GitHub: https://github.com/devunionorg
 * All rights reserved.
 *
 * This project was conceptualized and developed by @abdelillahbel.
 * GitHub: https://github.com/abdelillahbel
 */

package dev.devunion.skillsnap.viewmodels.db

import dev.devunion.skillsnap.models.UserInfo


interface FirestoreViewModelInterface {

    fun isUsernameAvailable(
        username: String,
        onSuccess: (Boolean) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun fetchUsernameByUserId(
        userId: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun deleteUserProfile(
        userId: String,
        username: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun updateHasProfileFlag(
        userId: String,
        hasProfile: Boolean,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )


    fun checkUserHasProfile(
        userId: String,
        onSuccess: (Boolean) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun fetchUserProfile(
        userId: String,  // This is the new method we're adding
        onSuccess: (UserInfo) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun fetchUserInfo(
        username: String,
        onSuccess: (UserInfo) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun saveUserInfo(
        userInfo: UserInfo,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )
}
