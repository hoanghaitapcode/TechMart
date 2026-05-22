package com.springboot.techmart.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.springboot.techmart.dto.response.ProductImageResponse;
import com.springboot.techmart.entity.Product;
import com.springboot.techmart.entity.ProductImage;
import com.springboot.techmart.entity.Role;
import com.springboot.techmart.entity.User;
import com.springboot.techmart.exception.BadRequestException;
import com.springboot.techmart.repository.ProductImageRepository;
import com.springboot.techmart.repository.ProductRepository;
import com.springboot.techmart.repository.UserRepository;
import com.springboot.techmart.security.SecurityUtils;
import com.springboot.techmart.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final Cloudinary cloudinary;
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png", "image/gif","image/webp");
    private static final int MAX_STATIC_PER_PRODUCT = 8;

    @Override
    @Transactional
    public ProductImageResponse uploadProductImage(UUID productId, MultipartFile file, String altText, Integer sortOrder, Boolean isPrimary) {
        validateFile(file);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy sản phẩm"));
        checkOwnership(product);
        //check so anh
        int currentCount = productImageRepository.countByProductId(productId);
        if(currentCount>=MAX_STATIC_PER_PRODUCT) {
            throw new BadRequestException("Mỗi sản phẩm chỉ được có tối đa " + MAX_STATIC_PER_PRODUCT + " ảnh");
        }
        Map uploadResult;
        try {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "techmart/products/" + productId,
                    "resource_type", "image"
            ));
        } catch (IOException e) {
            throw new BadRequestException("Lỗi khi upload ảnh: " + e.getMessage());
        }
        if(isPrimary==true) {
            Optional<ProductImage> optionalOldPrimary = productImageRepository.findByProductIdAndIsPrimaryTrue(productId);
            if(optionalOldPrimary.isPresent()) {
                ProductImage oldPrimary = optionalOldPrimary.get();
                oldPrimary.setIsPrimary(false);
                productImageRepository.save(oldPrimary);
            }
        }

        if (currentCount ==0) {
            isPrimary = true;
        }
        //luu vao db
        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setImageUrl((String) uploadResult.get("secure_url"));
        image.setPublicId((String) uploadResult.get("public_id"));
        image.setAltText(altText);
        image.setSortOrder(sortOrder);
        image.setIsPrimary(isPrimary);
        productImageRepository.save(image);

        return ProductImageResponse.fromEntity(image);
    }


    @Override
    @Transactional
    public void deleteProductImage(UUID productId, UUID imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy ảnh"));
        if(!image.getProduct().getId().equals(productId)) {
            throw new BadRequestException("Ảnh không thuộc về sản phẩm");
        }
        try {
            cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.asMap(
                    "resource_type", "image"
            ));
        } catch (IOException e) {
            throw new BadRequestException("Lỗi khi xóa ảnh: " + e.getMessage());
        }
        productImageRepository.delete(image);
    }

    @Override
    @Transactional
    public ProductImageResponse setPrimaryImage(UUID productId, UUID imageId) {
        ProductImage  currentImage =  productImageRepository.findById(imageId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy ảnh"));
        if(!currentImage.getProduct().getId().equals(productId)) {
            throw new BadRequestException("Ảnh không thuộc về sản phẩm");
        }

        checkOwnership(currentImage.getProduct());


        Optional<ProductImage> optionalImage = productImageRepository.findByProductIdAndIsPrimaryTrue(imageId);
        if(optionalImage.isPresent()) {
            ProductImage currentPrimary = optionalImage.get();
            currentPrimary.setIsPrimary(false);
            productImageRepository.save(currentPrimary);
        }

        currentImage.setIsPrimary(true);
        productImageRepository.save(currentImage);
        return ProductImageResponse.fromEntity(currentImage);
    }

    public void validateFile(MultipartFile file) {
        if(file==null||file.isEmpty()) {
            throw new BadRequestException("File không được để trống");
        }
        if(file.getSize()>MAX_FILE_SIZE) {
            throw new BadRequestException("File vượt quá kích thước tối đa 5MB");
        }
        if(!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new BadRequestException("File phải có định dạng JPEG, PNG, GIF hoặc WEBP");
        }


    }
    public void checkOwnership(Product product) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy người dùng"));
        if(currentUser.getRole()!= Role.ADMIN || !currentUserId.equals(product.getVendor().getId())) {
            throw new BadRequestException("Bạn không có quyền thực hiện hành động này");
        }

    }
}
