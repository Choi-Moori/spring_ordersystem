package beyond.ordersystem.product.controller;

import beyond.ordersystem.common.dto.CommonResDto;
import beyond.ordersystem.product.domain.Product;
import beyond.ordersystem.product.dto.ProductCreateReqDto;
import beyond.ordersystem.product.dto.ProductResDto;
import beyond.ordersystem.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> productCreate(ProductCreateReqDto dto){
        Product product = productService.productCreate(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "product is successfully created", product.getId());

        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    @PostMapping("/list")
    public ResponseEntity<?> productList(Pageable pageable){
        Page<ProductResDto> dtos = productService.productList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "product is Created", dtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }
}
