/*
 * Copyright (c) 2024. DevUnion Foundation.
 * GitHub: https://github.com/devunionorg
 * All rights reserved.
 *
 * This project was conceptualized and developed by @abdelillahbel.
 * GitHub: https://github.com/abdelillahbel
 */

package dev.devunion.skillsnap.ui.profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.devunion.skillsnap.R
import dev.devunion.skillsnap.models.Education
import dev.devunion.skillsnap.models.Experience
import dev.devunion.skillsnap.models.Project
import dev.devunion.skillsnap.models.UserInfo
import dev.devunion.skillsnap.utils.getBitmapFromUri
import dev.devunion.skillsnap.viewmodels.db.FirestoreViewModelInterface
import dev.devunion.skillsnap.viewmodels.storage.StorageViewModelInterface
import java.util.UUID


@Composable
fun UpdateProfileScreen(
    navController: NavController,
    viewModel: FirestoreViewModelInterface,
    storageViewModel: StorageViewModelInterface
) {

    val poppinsFamily = FontFamily(
        Font(R.font.poppins_light, FontWeight.Light),
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )

    val user = Firebase.auth.currentUser
    var userInfo by remember { mutableStateOf<UserInfo?>(null) }
    var username by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imageUri = uri
        }
    )
    val userId = user!!.uid

    // Fetch username based on userId
    LaunchedEffect(Unit) {
        viewModel.fetchUsernameByUserId(
            userId = userId,
            onSuccess = { fetchedUsername ->
                username = fetchedUsername
                // Fetch user data once username is obtained
                viewModel.fetchUserInfo(
                    username = fetchedUsername,
                    onSuccess = { fetchedUserInfo ->
                        userInfo = fetchedUserInfo
                    },
                    onFailure = { exception ->
                        Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                        Log.i("TAG", "UpdateProfileScreen: ${exception.message}")
                    }
                )
            },
            onFailure = { exception ->
                Toast.makeText(
                    context,
                    "Failed to fetch username: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i("TAG", "UpdateProfileScreen: ${exception.message}")
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {

        Text(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            text = "Update",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium
        )

        userInfo?.let { user ->

            // Avatar Image
            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = colorScheme.onBackground,
                            shape = CircleShape
                        )
                        .background(colorScheme.surface)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        }
                )
            } ?: run {
                Image(
                    painter = rememberAsyncImagePainter(user.avatar),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = colorScheme.onBackground,
                            shape = CircleShape
                        )
                        .background(colorScheme.surface)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "General information",
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(6.dp))

            // Updating user details like name, bio, etc.
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = user.name,
                onValueChange = { newName ->
                    userInfo = user.copy(name = newName)
                },
                label = { Text("Name") }
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = user.bio,
                onValueChange = { newBio ->
                    userInfo = user.copy(bio = newBio)
                },
                label = { Text("Bio") }
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = user.about,
                onValueChange = { newAbout ->
                    userInfo = user.copy(about = newAbout)
                },
                label = { Text("About") }
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = user.resume,
                onValueChange = { newResume ->
                    userInfo = user.copy(resume = newResume)
                },
                label = { Text("Resume link") }
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Contact details",
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            // Contact Information Section
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = user.contact.email,
                onValueChange = { newEmail ->
                    userInfo = user.copy(contact = user.contact.copy(email = newEmail))
                },
                label = { Text("Email") }
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = user.contact.phone,
                onValueChange = { newPhone ->
                    userInfo = user.copy(contact = user.contact.copy(phone = newPhone))
                },
                label = { Text("Phone") }
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = user.contact.linkedin,
                onValueChange = { newLinkedIn ->
                    userInfo = user.copy(contact = user.contact.copy(linkedin = newLinkedIn))
                },
                label = { Text("LinkedIn") }
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = user.contact.github,
                onValueChange = { newGithub ->
                    userInfo = user.copy(contact = user.contact.copy(github = newGithub))
                },
                label = { Text("GitHub") }
            )


            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .padding(16.dp, 8.dp),
                text = "We're sorry, username cannot be changed (${user.username}).",
                style = MaterialTheme.typography.bodyLarge
            )
            // Project Section
            ProjectSection(
                projects = user.projects,
                onAddProject = { newProject ->
                    val updatedProjects = user.projects.toMutableMap()
                    val projectId = UUID.randomUUID().toString()
                    updatedProjects[projectId] = newProject
                    userInfo = user.copy(projects = updatedProjects)
                },
                onUpdateProject = { id, updatedProject ->
                    val updatedProjects = user.projects.toMutableMap()
                    updatedProjects[id] = updatedProject
                    userInfo = user.copy(projects = updatedProjects)
                }
            )

            // Education Section
            EducationSection(
                education = user.education,
                onAddEducation = { newEducation ->
                    val updatedEducation = user.education.toMutableMap()
                    val educationId = UUID.randomUUID().toString()
                    updatedEducation[educationId] = newEducation
                    userInfo = user.copy(education = updatedEducation)
                },
                onUpdateEducation = { id, updatedEducation ->
                    val updatedEducationMap = user.education.toMutableMap()
                    updatedEducationMap[id] = updatedEducation
                    userInfo = user.copy(education = updatedEducationMap)
                }
            )

            // Experience Section
            ExperienceSection(
                experience = user.experience,
                onAddExperience = { newExperience ->
                    val updatedExperience = user.experience.toMutableMap()
                    val experienceId = UUID.randomUUID().toString()
                    updatedExperience[experienceId] = newExperience
                    userInfo = user.copy(experience = updatedExperience)
                },
                onUpdateExperience = { id, updatedExperience ->
                    val updatedExperienceMap = user.experience.toMutableMap()
                    updatedExperienceMap[id] = updatedExperience
                    userInfo = user.copy(experience = updatedExperienceMap)
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    imageUri?.let { uri ->
                        val bitmap = getBitmapFromUri(context, uri)
                        bitmap?.let {
                            storageViewModel.uploadImage(it,
                                onSuccess = { downloadUrl ->
                                    val updatedUserInfo = user.copy(avatar = downloadUrl)
                                    viewModel.saveUserInfo(
                                        userInfo = updatedUserInfo,
                                        onSuccess = {
                                            Toast.makeText(
                                                context,
                                                "Profile updated successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.popBackStack()
                                        },
                                        onFailure = { exception ->
                                            Toast.makeText(
                                                context,
                                                "Error: ${exception.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                },
                                onFailure = { exception ->
                                    Toast.makeText(
                                        context,
                                        "Error: ${exception.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                        }
                    } ?: run {
                        // If no image is selected, just save the rest of the data
                        viewModel.saveUserInfo(
                            userInfo = user,
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Profile updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.popBackStack()
                            },
                            onFailure = { exception ->
                                Toast.makeText(
                                    context,
                                    "Error: ${exception.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            ) {
                Text("Update Profile")
            }

        } ?: run {
            // Handle the case when userInfo is null, e.g., show a loading indicator or message
            // Show loading indicator in the center of the screen
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun ExperienceSection(
    experience: Map<String, Experience>,
    onAddExperience: (Experience) -> Unit,
    onUpdateExperience: (String, Experience) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Experience", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        experience.forEach { (id, experience) ->
            ExperienceItem(
                experience = experience,
                onUpdateExperience = { updatedExperience ->
                    onUpdateExperience(id, updatedExperience)
                }, onAddExperience = { addExperience ->
                    // onAddExperience(id, addExperience) TODO

                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            onAddExperience(
                Experience(
                    title = "New Experience",
                    company = "Company Name",
                    period = "2022-2024",
                    description = "Experience Example"
                )
            )
        }) {
            Text("Add New Experience")
        }
    }
}

@Composable
fun ExperienceItem(
    experience: Experience,
    onUpdateExperience: (Experience) -> Unit,
    onAddExperience: (Experience) -> Unit
) {
    var title by remember { mutableStateOf(experience.title) }
    var company by remember { mutableStateOf(experience.company) }
    var period by remember { mutableStateOf(experience.period) }
    var description by remember { mutableStateOf(experience.description) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                onUpdateExperience(experience.copy(title = it))
            },
            label = { Text("Title") }
        )

        OutlinedTextField(
            value = company,
            onValueChange = {
                company = it
                onUpdateExperience(experience.copy(company = it))
            },
            label = { Text("Company") }
        )

        OutlinedTextField(
            value = period,
            onValueChange = {
                period = it
                onUpdateExperience(experience.copy(period = it))
            },
            label = { Text("Period") }
        )

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                onUpdateExperience(experience.copy(description = it))
            },
            label = { Text("Description") },
            modifier = Modifier.height(100.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun EducationSection(
    education: Map<String, Education>,
    onAddEducation: (Education) -> Unit,
    onUpdateEducation: (String, Education) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Education", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        education.forEach { (id, education) ->
            EducationItem(
                education = education,
                onUpdateEducation = { updatedEducation ->
                    onUpdateEducation(id, updatedEducation)
                },
                onAddEducation = { addEducation ->
                    // onAddEducation(id, addEducation) TODO
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            onAddEducation(
                Education(
                    degree = "New Degree",
                    institution = "Institution",
                    year = "2022-2024"
                )
            )
        }) {
            Text("Add New Education")
        }
    }
}

@Composable
fun EducationItem(
    education: Education,
    onUpdateEducation: (Education) -> Unit,
    onAddEducation: (Education) -> Unit
) {
    var degree by remember { mutableStateOf(education.degree) }
    var institution by remember { mutableStateOf(education.institution) }
    var year by remember { mutableStateOf(education.year) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = degree,
            onValueChange = {
                degree = it
                onUpdateEducation(education.copy(degree = it))
            },
            label = { Text("Degree") }
        )

        OutlinedTextField(
            value = institution,
            onValueChange = {
                institution = it
                onUpdateEducation(education.copy(institution = it))
            },
            label = { Text("Institution") }
        )

        OutlinedTextField(
            value = year,
            onValueChange = {
                year = it
                onUpdateEducation(education.copy(year = it))
            },
            label = { Text("Year") }
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ProjectSection(
    projects: Map<String, Project>,
    onAddProject: (Project) -> Unit,
    onUpdateProject: (String, Project) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Projects", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        projects.forEach { (id, project) ->
            ProjectItem(
                project = project,
                onUpdateProject = { updatedProject ->
                    onUpdateProject(id, updatedProject)
                },
                onAddProject = {
                    // TODO
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            onAddProject(
                Project(
                    title = "New Project",
                    description = "Project Description",
                    image = null
                )
            )
        }) {
            Text("Add New Project")
        }
    }
}

@Composable
fun ProjectItem(
    project: Project,
    onUpdateProject: (Project) -> Unit,
    onAddProject: (Project) -> Unit
) {
    var title by remember { mutableStateOf(project.title) }
    var description by remember { mutableStateOf(project.description) }
    var imageLink by remember { mutableStateOf(project.image ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                onUpdateProject(project.copy(title = it))
            },
            label = { Text("Title") }
        )

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                onUpdateProject(project.copy(description = it))
            },
            label = { Text("Description") },
            modifier = Modifier.height(100.dp)
        )

        OutlinedTextField(
            value = imageLink,
            onValueChange = {
                imageLink = it
                onUpdateProject(project.copy(image = it))
            },
            label = { Text("Image Link") }
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}
