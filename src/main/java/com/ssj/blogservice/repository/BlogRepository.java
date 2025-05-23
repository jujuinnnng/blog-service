package com.ssj.blogservice.repository;

import com.ssj.blogservice.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
