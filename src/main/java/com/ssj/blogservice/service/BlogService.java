package com.ssj.blogservice.service;

import com.ssj.blogservice.domain.Article;
import com.ssj.blogservice.dto.AddArticleRequest;
import com.ssj.blogservice.dto.UpdateArticleRequest;
import com.ssj.blogservice.repository.BlogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    //글 추가
    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    //글 조회(전체)
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    //글 조회(하나)
    public Article findById(long id){
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    // 글 삭제
    public void delete(long id){
        blogRepository.deleteById(id);
    }

    //글 수정
    @Transactional
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }
}
