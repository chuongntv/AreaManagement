package com.chuongntv.areaapp.loader;

import com.chuongntv.areaapp.models.City;
import com.chuongntv.areaapp.models.Country;
import com.chuongntv.areaapp.models.District;
import com.chuongntv.areaapp.repositories.CityRepository;
import com.chuongntv.areaapp.repositories.CountryRepository;
import com.chuongntv.areaapp.repositories.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
/**
 * Created by chuongntv on 12/17/15.
*/

@Component
public class AppLoader implements ApplicationListener<ContextRefreshedEvent>{
    private CountryRepository countryRepository;
    private CityRepository cityRepository;
    private DistrictRepository districtRepository;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Country vn = new Country("Vietnam","vi");
        countryRepository.save(vn);
        Country us = new Country("United States","us");
        countryRepository.save(us);
        Country ml = new Country("Malaysia","ml");
        countryRepository.save(ml);
        City dn = new City("Da Nang","0511",vn);
        cityRepository.save(dn);
        City hcm = new City("Ho Chi Minh","08",vn);
        cityRepository.save(hcm);
        City hn = new City("Ha Noi","04",vn);
        cityRepository.save(hn);
        City hp = new City("Hai Phong","45",vn);
        cityRepository.save(hp);
        City ny = new City("New Your","901",us);
        cityRepository.save(ny);
        District hc = new District("Hai Chau","01",dn);
        districtRepository.save(hc);
        District tk = new District("Thanh Khe","04",dn);
        districtRepository.save(tk);
        District cl = new District("Cam Le","06",dn);
        districtRepository.save(cl);
        District hv = new District("Hoa Vang","44",dn);
        districtRepository.save(tk);
    }
    @Autowired
    public void setCountryRepository(CountryRepository countryRepository)
    {
        this.countryRepository=countryRepository;
    }
    @Autowired
    public void setCityRepository(CityRepository cityRepository)
    {
        this.cityRepository=cityRepository;
    }
    @Autowired
    public void setDistrictRepository(DistrictRepository districtRepository){this.districtRepository=districtRepository;}
}
