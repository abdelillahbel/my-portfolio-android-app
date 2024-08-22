package dev.devunion.myportfolio.models


data class UserInfo(
    val id: String, // User ID from FirebaseAuth
    val username: String,
    val name: String,
    val status: String, // e.g., visitor, member, etc.
    val avatar: String,
    val active: Boolean,
    val visible: Boolean,
    val resume: String?,
    val bio: String,
    val role: String,
    val about: String,
    val education: Map<String, Education>,
    val experience: Map<String, Experience>,
    val projects: Map<String, Project>,
    val contact: Contact,
    val createdAt: Long // Timestamp of creation
)

data class Education(
    val degree: String,
    val institution: String,
    val year: String
)

data class Experience(
    val title: String,
    val company: String,
    val period: String, // e.g., "2019 - 2024"
    val description: String
)

data class Project(
    val title: String,
    val description: String,
    val image: String?
)

data class Contact(
    val email: String,
    val phone: String?,
    val linkedin: String?,
    val github: String?
)

//val userInfo = UserInfo(
//    id = "auth_user_id", // This should come from FirebaseAuth
//    username = "maria_ds",
//    name = "Maria Dos Santos",
//    status = "visitor",
//    avatar = "https://randomuser.me/api/portraits/women/50.jpg",
//    resume = "https://www.hloom.com/sample.pdf",
//    bio = "Passionate UX/UI designer with a strong background in creating user-centered designs.",
//    role = "Lead Designer",
//    about = "Maria has 5 years of experience in the design field, specializing in UX/UI for mobile applications.",
//    education = mapOf(
//        "0" to Education(
//            degree = "Bachelor of Design",
//            institution = "University of Sao Paulo",
//            year = "2019"
//        )
//    ),
//    experience = mapOf(
//        "0" to Experience(
//            title = "Lead UX/UI Designer",
//            company = "TechWave",
//            period = "2019 - 2024",
//            description = "Designing and prototyping mobile and web applications."
//        ),
//        "1" to Experience(
//            title = "Junior Graphic Designer",
//            company = "Creative Studios",
//            period = "2017 - 2019",
//            description = "Worked on branding and visual identity for various clients."
//        )
//    ),
//    projects = mapOf(
//        "0" to Project(
//            title = "E-commerce Redesign",
//            description = "Complete overhaul of an e-commerce platform's user interface.",
//            image = ""
//        ),
//        "1" to Project(
//            title = "Mobile Banking App",
//            description = "Designing a user-friendly mobile banking experience.",
//            image = ""
//        )
//    ),
//    contact = Contact(
//        email = "maria_ds@gmail.com",
//        phone = "0025612345678",
//        linkedin = "https://www.linkedin.com/in/maria-dos-santos",
//        github = "https://github.com/maria-dos-santos"
//    ),
//    createdAt = System.currentTimeMillis() // Current timestamp
//)
