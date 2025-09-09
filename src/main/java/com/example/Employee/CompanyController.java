package com.example.Employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("companies")
public class CompanyController {
    private List<String> companyList = new ArrayList<>();

    @GetMapping
    public List<String> getCompanies(@RequestParam(required = false) String id) {
        if(id != null) {
            int companyId = Integer.parseInt(id);
            if(companyId >= 0 && companyId < companyList.size()) {
                List<String> result = new ArrayList<>();
                result.add(companyList.get(companyId));
                return result;
            } else {
                return new ArrayList<>();
            }
        }
        return companyList;
    }

    @PostMapping
    public void addCompany(@RequestBody String company) {
        companyList.add(company);
    }
}
