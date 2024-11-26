package comment.bean;

public enum CommentStatus {
    DEFAULT(0, "일반 댓글"),
    EDITED(1, "수정된 댓글"),
    DELETED(99, "삭제된 댓글");

    private final int code;
    private final String description;

    CommentStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static CommentStatus getCommentStatus(int code) {

        for (CommentStatus status : CommentStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid CommentStatus code: " + code);
    }
}
