package com.Bootcamp_Project.ECommerce.repos;

import com.Bootcamp_Project.ECommerce.entities.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category , Long> {
    Optional<Category> findByName(String name);
    boolean existsByName(String name);
    List<Category> findByParentCategory(Category category);
    @Query(value = "SELECT * from category where id Not IN (Select parent_category_id from category where parent_category_id IS NOT NULL) ",nativeQuery = true)
    List<Category> findByIdLeaf();
    List<Category> findAllByParentCategory(Category category);

    boolean existsByParentCategory(Category category);


}
