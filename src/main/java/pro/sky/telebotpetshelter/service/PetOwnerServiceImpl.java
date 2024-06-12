package pro.sky.telebotpetshelter.service;

import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import pro.sky.telebotpetshelter.entity.PetOwner;
//import pro.sky.telebotpetshelter.entity.animals.Cat;
//import pro.sky.telebotpetshelter.exceptions.NotFoundException;
import pro.sky.telebotpetshelter.exceptions.OwnerNotFoundException;
import pro.sky.telebotpetshelter.repository.PetOwnerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PetOwnerServiceImpl implements PetOwnerService {

    private final PetOwnerRepository petOwnerRepository;
    private final Logger logger = LoggerFactory.getLogger(PetOwnerService.class);

    public PetOwnerServiceImpl(PetOwnerRepository petOwnerRepository) {
        this.petOwnerRepository = petOwnerRepository;
    }

    @Override
    public PetOwner addOwner(PetOwner petOwner) {
        logger.info("Был вызван метод addOwner");
        return petOwnerRepository.save(petOwner);
    }

    @Override
    public PetOwner getOwnerById(Long id) {
        logger.info("Был вызван метод getOwnerById");
        Optional<PetOwner> petOwner = petOwnerRepository.findById(id);
        if (petOwner.isEmpty()) {
            throw new OwnerNotFoundException(String.format("Owner [%s] not found", id));
        }
        return petOwner.get();
//        return petOwnerRepository.findById(id).get();

    }

    @Override
    public List<PetOwner> getAllOwners() {
        logger.info("Был вызван метод getAllOwners");
        return petOwnerRepository.findAll();
    }

    // Nik редактировал метод updateOwner. Без этого не работал тест
    @Override
    public PetOwner updateOwner(PetOwner petOwner) {
        logger.info("Был вызван метод updateOwner");
        Optional<PetOwner> OwnerId = petOwnerRepository.findById(petOwner.getTelegramId());
        if (OwnerId.isEmpty()) {
            throw new OwnerNotFoundException("Такого владельца нет");
        }
        PetOwner currentOwner = OwnerId.get();

        if (petOwner.getName() != null) {
            currentOwner.setName(petOwner.getName());
        }
        if (petOwner.getEmail() != null) {
            currentOwner.setEmail(petOwner.getEmail());
        }
        if (petOwner.getPhoneNumber() != null) {
            currentOwner.setPhoneNumber(petOwner.getPhoneNumber());
        }
        currentOwner.setTookAnAnimal(petOwner.isTookAnAnimal());

        return petOwnerRepository.save(currentOwner);
    }

    @Override
    public void deleteOwner(Long id) {
        logger.info("Был вызван метод deleteOwner");
        petOwnerRepository.deleteById(id);
    }

//    @Override
//    public void registerUser(Update update) {
//        Long chatId = update.message().chat().id();
//        PetOwner petOwner = new PetOwner();
//        petOwner.setTelegramId(chatId);
//        petOwner.setFirstName(update.message().chat().firstName());
//        petOwnerRepository.save(petOwner);
//    }
//
//    @Override
//    public boolean newUser(Update update) {
//        return !(petOwnerRepository.existsById(update.message().chat().id()));
//    }

    @Override
    public boolean existsById(Long id) {
        return petOwnerRepository.existsById(id);
    }
}
