package com.nokshal.amarDesh.content.repository;

import com.nokshal.amarDesh.content.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByCategory(String category);
    List<Video> findByAuthorId(Long authorId);
    List<Video> findByStatus(String status);
}
