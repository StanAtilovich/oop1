import org.jetbrains.annotations.Nullable
import java.awt.event.WindowListener


data class Comment(
    @Nullable
    val id: Int,
    val postId: Int,
    val date: Int,
    val text: String,
    val attachments: Array<Attachment>? = null
)

class PostNotFoundException(message: String) : RuntimeException(message)
class CommentNotFoundException(message: String) : RuntimeException(message)

class ReasonNotFoundException(message: String) : RuntimeException(message)
data class Post(
    @Nullable
    val id: Int,
    val contentL: String,
    val likes: Int = 0,
    val attachments: Array<Attachment> = emptyArray(),
    val ownerId: Int? = 0,
    val date: Long,
    val views: Int? = 0,
    val comments: Int? = null

) {
    fun printContent() {
        println("Post with$id printted it content:$contentL")
    }
}

data class Complain(
    val ownerId: Int,
    val commentId: Int,
    val reason : ReasonsComplain? = null
)
enum class ReasonsComplain(
    val id: Int,
    val text: String
){
    SPAM(0, "спам"),
    CHLDPRN(1, "детская порнография"),
    EXTREMISM(2, "экстремизм"),
    VIOLENCE(3, "насилие"),
    DRUGS(4, "пропаганда наркотиков"),
    ADULT(5, "материал для взрослых"),
    ABUSE(6, "оскорбление"),
    SUICIDE(8, "призывы к суициду");
}
data class Audio(
    val id: Int,
    val name: String
)

data class Video(
    val id: Int,
    val lenght: Int
)

data class Donut(
    val isDonut: Boolean = false,
    val paidDuration: Int = 0,
    val placeholder: String = "",
    val canPublishFreeCopy: Boolean = false,
    val editMode: String
)

interface Attachment {
    val type: String
}

data class AudioAtachment(val audio: Audio) : Attachment {
    override val type = "audio"
}


data class VideoAtachment(val video: Video) : Attachment {
    override val type = "video"
}

data class DonutAtachment(
    val dunut: Donut,
    val isDonut: Boolean = false,
    val paidDuration: Int = 0,
    val canPublishFreeCopy: Boolean = false,
    val editMode: String = " "
) : Attachment {
    override val type = "donut"
}

data class RepostsAtachment(
    val count: Long,
    val userReposted: Boolean
) : Attachment {
    override val type = "reposts"
}

object WallService {
    private var posts = emptyArray<Post>()
    private var comments = emptyArray<Comment>()
    private var complains = emptyArray<Complain>()
    fun createComment(comment: Comment): Boolean {
        var postFound = false
        for (post in posts){
            if (post.id == comment.postId){
                postFound = true
                comments += comment
                println("коментарий добавлен ${comment.postId}(${comment.text})")
            }
        }
        if (!postFound){
            throw PostNotFoundException("добавление коментария невозможно не существует ${comment.postId}")
        }
        return true
    }

    fun add(post: Post) {
        posts += post
    }

    fun update(postFind: Post): Boolean {
        for ((index, post) in posts.withIndex()) {
            if (post.id == postFind.id) {
                posts[index] = postFind.copy(id = post.id, date = post.date)
                return true
            }
        }
        return false
    }


    fun like(postId: Int): Boolean {
        for ((index, post) in posts.withIndex()) {
            if (post.id == postId) {
                posts[index] = post.copy(likes = post.likes + 1)
                return true
            }
        }
        return false
    }


    fun print() {
        for (post in posts) {
            println(post)
        }
        println()
    }

    fun makeComplain(complain: Complain): Int {
        var commentIdFound = false
        for (comment in comments) {
            if (comment.id == complain.commentId){
                commentIdFound = true
            }
        }
        if (complain.reason == null)throw  ReasonNotFoundException("невозможно подать жалобу укажи причину")
        if (!commentIdFound)throw  CommentNotFoundException("невозможно подать жалобу (${complain.reason.text}, код причины: ${complain.reason.id}): Комменария с id ${complain.commentId} не существует!")
        complains += complain
        println("жалоба подана (Комменарий id: ${complain.commentId}. Причина: ${complain.reason.text}, код причины: ${complain.reason.id})")
        return 1
    }
}


fun main() {
    val post = Post(
        1,
        "Шупашкара киль",
        attachments = arrayOf(
            AudioAtachment(Audio(1, "song.mp3")),
            RepostsAtachment(23, false),
            DonutAtachment(Donut(true, 455, "Салам", true, "all")),
        ),
        likes = 99,
        ownerId = 455555,
        date = 17102022,
        views = (458)
    )

    post.printContent()
    println((post.attachments[0] as AudioAtachment).audio)

    WallService.add(post)
    WallService.update(post)
    WallService.add(
        Post(
            2,
            "Я Чуваш!",
            attachments = arrayOf(
                AudioAtachment(Audio(2, "anothersong.mp3")),
                VideoAtachment(Video(2, 60)),
                RepostsAtachment(23, false),
                (DonutAtachment(Donut(true, 700, "Ачисем", true, "all")))
            ),
            likes = 78,
            ownerId = 789,
            date = 181022,
            views = 635
        )
    )

    post.printContent()
    println((post.attachments[0] as AudioAtachment).audio)

    WallService.add(post)
    WallService.add(
        Post(
            3,
            "Как свалить из России?",
            attachments = arrayOf(
                AudioAtachment(Audio(3, "чакапущ.mp3")),
                VideoAtachment(Video(3, 30)),
                RepostsAtachment(25, false),
                (DonutAtachment(Donut(true, 800, "Атилла рулит", true, "all")))
            ),
            likes = 0,
            ownerId = 999,
            date = 191022,
            views = 1000
        )
    )

    post.printContent()
    println((post.attachments[0] as AudioAtachment).audio)

    WallService.add(post)
    WallService.add(

        Post(
            4,
            "сколько стоит квартира в Караганде?",
            attachments = arrayOf(
                AudioAtachment(Audio(3, "Тупах яле.mp3")),
                VideoAtachment(Video(3, 40)),
                RepostsAtachment(26, false),
                (DonutAtachment(Donut(true, 700, "Дацин", true, "all")))
            ),
            likes = 70000,
            ownerId = 11,
            date = 201022,
            views = 10
        )
    )


    post.printContent()
    println((post.attachments[0] as AudioAtachment).audio)

    WallService.add(post)
    WallService.add(

        Post(
            5,
            "ылуоси?",
            attachments = arrayOf(
                AudioAtachment(Audio(4, "Турапа перле.mp3")),
                VideoAtachment(Video(4, 1000)),
                RepostsAtachment(27, false),
                (DonutAtachment(Donut(true, 99900, "Чайна", true, "all")))
            ),
            likes = 7000,
            ownerId = 12,
            date = 211022,
            views = 1770
        )
    )


    WallService.print()
    WallService.like(2)
    WallService.print()

    WallService.createComment(Comment(1,2,51122,"Salam",null))

    try {
        WallService.createComment(Comment(65,454,61122,"sass",null))
        }catch (e: PostNotFoundException){
            println(e.message)
        }
    try {
        WallService.makeComplain(Complain(0,5, ReasonsComplain.ABUSE))
    }catch(e: CommentNotFoundException){
        println(e.message)
    }
    WallService.makeComplain(Complain(1,1, ReasonsComplain.SPAM))
    WallService.makeComplain(Complain(1,1, ReasonsComplain.DRUGS))
    WallService.makeComplain(Complain(1,1, ReasonsComplain.SUICIDE))

    try{
        WallService.makeComplain(Complain(0,1))
    }catch(e: ReasonNotFoundException){
        println(e.message)
    }
}