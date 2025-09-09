package com.example.Employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("companies")
public class CompanyController {
    private List<Company> companies = new ArrayList<>();
    private int id = 0;

    public void clear() {
        companies.clear();
        id = 0;
    }

    @GetMapping
    public List<Company> getCompanies(@RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer size) {
        // 分页处理
        if (page != null && size != null && page > 0 && size > 0) {
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, companies.size());

            if (startIndex >= companies.size()) {
                return new ArrayList<>();
            }

            return companies.subList(startIndex, endIndex);
        }

        return companies;
    }

    @GetMapping("{id}")
    public Company getCompany(@PathVariable int id) {
        for (Company company : companies) {
            if (company.id().equals(id)) {
                return company;
            }
        }
        return null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company create(@RequestBody Company company) {
        int newId = ++this.id;
        Company newCompany = new Company(newId, company.name());
        companies.add(newCompany);
        return newCompany;
    }

    @PutMapping("{id}")
    public Company update(@PathVariable int id, @RequestBody Company company) {
        for (int i = 0; i < companies.size(); i++) {
            if (companies.get(i).id().equals(id)) {
                Company updatedCompany = new Company(id, company.name());
                companies.set(i, updatedCompany);
                return updatedCompany;
            }
        }
        return null;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        companies.removeIf(company -> company.id().equals(id));
    }


    public Company addCompany(String name) {
        int newId = ++this.id;
        Company newCompany = new Company(newId, name);
        companies.add(newCompany);
        return newCompany;
    }
}
