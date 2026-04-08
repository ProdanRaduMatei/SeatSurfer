package com.seatsurfer.service;

import com.seatsurfer.entity.AdminUser;
import com.seatsurfer.entity.Floor;
import com.seatsurfer.entity.Seat;
import com.seatsurfer.entity.SeatType;
import com.seatsurfer.model.AdminRole;
import com.seatsurfer.repository.AdminUserRepository;
import com.seatsurfer.repository.FloorRepository;
import com.seatsurfer.repository.SeatRepository;
import com.seatsurfer.repository.SeatTypeRepository;
import com.seatsurfer.util.NameUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SeedDataService implements ApplicationRunner {

    private final AdminUserRepository adminUserRepository;
    private final FloorRepository floorRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final SeatRepository seatRepository;
    private final PasswordEncoder passwordEncoder;

    public SeedDataService(
        AdminUserRepository adminUserRepository,
        FloorRepository floorRepository,
        SeatTypeRepository seatTypeRepository,
        SeatRepository seatRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.adminUserRepository = adminUserRepository;
        this.floorRepository = floorRepository;
        this.seatTypeRepository = seatTypeRepository;
        this.seatRepository = seatRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedAdmins();
        Map<String, SeatType> seatTypes = seedSeatTypes();
        seedFloorsAndSeats(seatTypes);
    }

    private void seedAdmins() {
        if (adminUserRepository.count() > 0) {
            return;
        }
        createAdmin("adi", "Adi Marin", "adi@seatsurfer.local");
        createAdmin("gratiela", "Gratiela Pop", "gratiela@seatsurfer.local");
        createAdmin("silviu", "Silviu Ionescu", "silviu@seatsurfer.local");
    }

    private Map<String, SeatType> seedSeatTypes() {
        if (seatTypeRepository.count() > 0) {
            return seatTypeRepository.findAll().stream().collect(java.util.stream.Collectors.toMap(SeatType::getCode, value -> value));
        }
        SeatType standard = createSeatType("STANDARD", "Standard", "Fully equipped, two monitors and chair", "#86B840", true, false, true);
        SeatType standing = createSeatType("STANDING_DESK", "Standing Desk", "Standing desk with two monitors and chair", "#2FA39A", true, true, true);
        SeatType standingNoChair = createSeatType("STANDING_NO_CHAIR", "Standing Desk (No Chair)", "Standing desk with two monitors and no chair", "#E3A441", true, true, false);
        SeatType deskOnly = createSeatType("DESK_ONLY", "Desk Only", "Only desk and chair, no monitors", "#4E89D8", false, false, true);
        return Map.of(
            standard.getCode(), standard,
            standing.getCode(), standing,
            standingNoChair.getCode(), standingNoChair,
            deskOnly.getCode(), deskOnly
        );
    }

    private void seedFloorsAndSeats(Map<String, SeatType> seatTypes) {
        if (floorRepository.count() > 0 || seatRepository.count() > 0) {
            return;
        }

        Floor atlas = createFloor("Atlas Floor", "Open collaborative floor", 1);
        Floor ionel = createFloor("Ionel Floor", "Quiet focus area inspired by the vision mockup", 2);

        LocalDate today = LocalDate.now();
        addSeats(atlas, seatTypes.get("STANDARD"), today, List.of(
            cell(1, 2), cell(1, 3), cell(2, 2), cell(2, 3),
            cell(4, 5), cell(4, 6), cell(5, 5), cell(5, 6),
            cell(4, 10), cell(4, 11), cell(5, 10), cell(5, 11)
        ));
        addSeats(atlas, seatTypes.get("STANDING_DESK"), today, List.of(
            cell(2, 8), cell(2, 9), cell(3, 8), cell(3, 9)
        ));
        addSeats(atlas, seatTypes.get("DESK_ONLY"), today, List.of(
            cell(6, 3), cell(6, 4), cell(6, 7), cell(6, 8), cell(6, 12), cell(6, 13)
        ));

        addSeats(ionel, seatTypes.get("STANDARD"), today, List.of(
            cell(1, 2), cell(1, 3), cell(2, 2), cell(2, 3),
            cell(1, 12), cell(1, 13), cell(2, 12), cell(2, 13),
            cell(1, 16), cell(1, 17), cell(2, 16), cell(2, 17)
        ));
        addSeats(ionel, seatTypes.get("STANDING_DESK"), today, List.of(
            cell(3, 7), cell(3, 8), cell(4, 7), cell(4, 8)
        ));
        addSeats(ionel, seatTypes.get("STANDING_NO_CHAIR"), today, List.of(
            cell(3, 12), cell(4, 12), cell(3, 17), cell(4, 17)
        ));
        addSeats(ionel, seatTypes.get("DESK_ONLY"), today, List.of(
            cell(5, 4), cell(5, 5), cell(5, 6), cell(5, 7),
            cell(5, 9), cell(5, 10), cell(5, 11), cell(5, 12)
        ));
    }

    private void createAdmin(String username, String fullName, String email) {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(username);
        adminUser.setFullName(fullName);
        adminUser.setEmail(email);
        adminUser.setPasswordHash(passwordEncoder.encode("SeatSurfer!2026"));
        adminUser.setRole(AdminRole.ADMIN);
        adminUser.setActive(true);
        adminUserRepository.save(adminUser);
    }

    private SeatType createSeatType(String code, String name, String description, String colorHex, boolean hasMonitors, boolean standing, boolean requiresChair) {
        SeatType seatType = new SeatType();
        seatType.setCode(code);
        seatType.setName(name);
        seatType.setDescription(description);
        seatType.setColorHex(colorHex);
        seatType.setHasMonitors(hasMonitors);
        seatType.setStanding(standing);
        seatType.setRequiresChair(requiresChair);
        seatType.setActive(true);
        return seatTypeRepository.save(seatType);
    }

    private Floor createFloor(String name, String description, int sortOrder) {
        Floor floor = new Floor();
        floor.setName(name);
        floor.setDescription(description);
        floor.setSortOrder(sortOrder);
        floor.setActive(true);
        return floorRepository.save(floor);
    }

    private void addSeats(Floor floor, SeatType seatType, LocalDate effectiveFrom, List<int[]> cells) {
        for (int[] cell : cells) {
            Seat seat = new Seat();
            seat.setFloor(floor);
            seat.setSeatType(seatType);
            seat.setRowIndex(cell[0]);
            seat.setColumnIndex(cell[1]);
            seat.setLabel(NameUtils.seatLabel(cell[0], cell[1]));
            seat.setEffectiveFrom(effectiveFrom);
            seatRepository.save(seat);
        }
    }

    private int[] cell(int row, int column) {
        return new int[]{row, column};
    }
}
