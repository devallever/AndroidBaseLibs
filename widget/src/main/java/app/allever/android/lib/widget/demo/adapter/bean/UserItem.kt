package app.allever.android.lib.widget.demo.adapter.bean

class UserItem {
    var nickname = ""
    var id = 0
    var avatar = ""

    override fun toString(): String {
        return "[nickname = $nickname, id = $id, avatar = $avatar]"
    }
}