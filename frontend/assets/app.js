const config = window.SEATSURFER_CONFIG || {};
const apiBaseUrl = (config.apiBaseUrl || "").replace(/\/$/, "");
const today = new Date().toISOString().slice(0, 10);

const state = {
    publicDate: today,
    viewerName: "",
    floors: [],
    selectedFloorId: null,
    floorLayout: null,
    selectedSeat: null,
    dailyReport: null,
    admin: {
        token: localStorage.getItem("seatsurfer-admin-token") || "",
        profile: JSON.parse(localStorage.getItem("seatsurfer-admin-profile") || "null"),
        challengeId: null,
        otpDelivery: null,
        otpExpiresAt: null
    },
    adminFloors: [],
    seatTypes: [],
    layoutDraft: {
        rows: 6,
        columns: 12,
        cells: new Map(),
        selectedSeatTypeId: null,
        selectedMode: "paint"
    },
    loadReport: null,
    monthlyReport: null
};

const els = {
    bookingDate: document.querySelector("#bookingDate"),
    viewerName: document.querySelector("#viewerName"),
    publicMessage: document.querySelector("#publicMessage"),
    publicFloorCards: document.querySelector("#publicFloorCards"),
    mapTitle: document.querySelector("#mapTitle"),
    seatLegend: document.querySelector("#seatLegend"),
    seatMapFrame: document.querySelector("#seatMapFrame"),
    seatSelectionPanel: document.querySelector("#seatSelectionPanel"),
    dailyReportBody: document.querySelector("#dailyReportBody"),
    refreshPublicButton: document.querySelector("#refreshPublicButton"),
    refreshDailyReportButton: document.querySelector("#refreshDailyReportButton"),
    heroAvailability: document.querySelector("#heroAvailability"),
    heroLoad: document.querySelector("#heroLoad"),
    heroApiTarget: document.querySelector("#heroApiTarget"),
    loginForm: document.querySelector("#loginForm"),
    otpForm: document.querySelector("#otpForm"),
    adminUsername: document.querySelector("#adminUsername"),
    adminPassword: document.querySelector("#adminPassword"),
    otpCode: document.querySelector("#otpCode"),
    authSummary: document.querySelector("#authSummary"),
    adminStatusChip: document.querySelector("#adminStatusChip"),
    adminWorkspace: document.querySelector("#adminWorkspace"),
    adminMessage: document.querySelector("#adminMessage"),
    floorForm: document.querySelector("#floorForm"),
    floorId: document.querySelector("#floorId"),
    floorName: document.querySelector("#floorName"),
    floorDescription: document.querySelector("#floorDescription"),
    floorSortOrder: document.querySelector("#floorSortOrder"),
    adminFloorList: document.querySelector("#adminFloorList"),
    resetFloorFormButton: document.querySelector("#resetFloorFormButton"),
    seatTypeForm: document.querySelector("#seatTypeForm"),
    seatTypeId: document.querySelector("#seatTypeId"),
    seatTypeCode: document.querySelector("#seatTypeCode"),
    seatTypeName: document.querySelector("#seatTypeName"),
    seatTypeDescription: document.querySelector("#seatTypeDescription"),
    seatTypeColor: document.querySelector("#seatTypeColor"),
    seatTypeMonitors: document.querySelector("#seatTypeMonitors"),
    seatTypeStanding: document.querySelector("#seatTypeStanding"),
    seatTypeChair: document.querySelector("#seatTypeChair"),
    seatTypeList: document.querySelector("#seatTypeList"),
    resetSeatTypeFormButton: document.querySelector("#resetSeatTypeFormButton"),
    layoutFloorSelect: document.querySelector("#layoutFloorSelect"),
    layoutEffectiveDate: document.querySelector("#layoutEffectiveDate"),
    layoutRows: document.querySelector("#layoutRows"),
    layoutColumns: document.querySelector("#layoutColumns"),
    layoutPalette: document.querySelector("#layoutPalette"),
    layoutEditor: document.querySelector("#layoutEditor"),
    loadLayoutButton: document.querySelector("#loadLayoutButton"),
    clearLayoutButton: document.querySelector("#clearLayoutButton"),
    saveLayoutButton: document.querySelector("#saveLayoutButton"),
    adminReportDate: document.querySelector("#adminReportDate"),
    loadFromDate: document.querySelector("#loadFromDate"),
    loadToDate: document.querySelector("#loadToDate"),
    monthlyYear: document.querySelector("#monthlyYear"),
    monthlyMonth: document.querySelector("#monthlyMonth"),
    loadAdminReportsButton: document.querySelector("#loadAdminReportsButton"),
    downloadPdfButton: document.querySelector("#downloadPdfButton"),
    emailMonthlyReportButton: document.querySelector("#emailMonthlyReportButton"),
    loadReportPanel: document.querySelector("#loadReportPanel"),
    monthlyReportPanel: document.querySelector("#monthlyReportPanel")
};

document.querySelectorAll("[data-scroll-target]").forEach(button => {
    button.addEventListener("click", () => {
        document.getElementById(button.dataset.scrollTarget)?.scrollIntoView({behavior: "smooth"});
    });
});

bootstrap();

async function bootstrap() {
    els.bookingDate.value = state.publicDate;
    els.layoutEffectiveDate.value = today;
    els.adminReportDate.value = today;
    els.loadFromDate.value = today;
    els.loadToDate.value = addDays(today, 30);
    const now = new Date();
    els.monthlyYear.value = String(now.getFullYear());
    els.monthlyMonth.value = String(now.getMonth() + 1);
    els.heroApiTarget.textContent = apiBaseUrl || "same-origin /api";
    bindEvents();
    await loadPublicExperience();
    if (state.admin.token) {
        await restoreAdminSession();
    } else {
        renderAdminState();
    }
}

function bindEvents() {
    els.refreshPublicButton.addEventListener("click", async () => {
        state.publicDate = els.bookingDate.value || today;
        state.viewerName = els.viewerName.value.trim();
        await loadPublicExperience();
    });

    els.refreshDailyReportButton.addEventListener("click", loadDailyReport);

    els.loginForm.addEventListener("submit", handleLoginStart);
    els.otpForm.addEventListener("submit", handleOtpVerify);

    els.floorForm.addEventListener("submit", handleFloorSubmit);
    els.resetFloorFormButton.addEventListener("click", resetFloorForm);

    els.seatTypeForm.addEventListener("submit", handleSeatTypeSubmit);
    els.resetSeatTypeFormButton.addEventListener("click", resetSeatTypeForm);

    els.layoutRows.addEventListener("change", syncDraftDimensionsFromInputs);
    els.layoutColumns.addEventListener("change", syncDraftDimensionsFromInputs);
    els.loadLayoutButton.addEventListener("click", loadAdminLayoutIntoDraft);
    els.clearLayoutButton.addEventListener("click", clearDraft);
    els.saveLayoutButton.addEventListener("click", saveDraftLayout);

    els.loadAdminReportsButton.addEventListener("click", loadAdminReports);
    els.downloadPdfButton.addEventListener("click", downloadPdfReport);
    els.emailMonthlyReportButton.addEventListener("click", emailMonthlyReport);
}

async function apiFetch(path, options = {}, requiresAuth = false) {
    const headers = new Headers(options.headers || {});
    headers.set("Content-Type", headers.get("Content-Type") || "application/json");
    if (requiresAuth && state.admin.token) {
        headers.set("Authorization", `Bearer ${state.admin.token}`);
    }

    const response = await fetch(`${apiBaseUrl}${path}`, {...options, headers});
    if (!response.ok) {
        let message = `Request failed with status ${response.status}`;
        try {
            const error = await response.json();
            message = error.message || message;
        } catch (_error) {
        }
        throw new Error(message);
    }

    if (response.status === 204) {
        return null;
    }

    const contentType = response.headers.get("content-type") || "";
    if (contentType.includes("application/pdf")) {
        return response.blob();
    }
    return response.json();
}

async function loadPublicExperience() {
    try {
        setMessage(els.publicMessage, "Loading live availability...");
        state.viewerName = els.viewerName.value.trim();
        state.floors = await apiFetch(`/api/public/floors?date=${state.publicDate}`);
        renderPublicFloors();
        updateHeroMetrics();
        if (!state.selectedFloorId && state.floors.length) {
            state.selectedFloorId = state.floors[0].id;
        }
        if (state.selectedFloorId) {
            await loadFloorLayout();
        } else {
            state.floorLayout = null;
            renderSeatMap();
        }
        await loadDailyReport();
        setMessage(els.publicMessage, "Live seat data is up to date.");
    } catch (error) {
        setMessage(els.publicMessage, error.message, true);
    }
}

async function loadDailyReport() {
    try {
        state.dailyReport = await apiFetch(`/api/public/reports/daily?date=${state.publicDate}`);
        renderDailyReport();
    } catch (error) {
        setMessage(els.publicMessage, error.message, true);
    }
}

async function loadFloorLayout() {
    if (!state.selectedFloorId) {
        return;
    }
    state.floorLayout = await apiFetch(
        `/api/public/floors/${state.selectedFloorId}/availability?date=${state.publicDate}&viewerName=${encodeURIComponent(state.viewerName || "")}`
    );
    state.selectedSeat = null;
    renderSeatMap();
}

function renderPublicFloors() {
    if (!state.floors.length) {
        els.publicFloorCards.innerHTML = `<p class="empty-state">No active floors are configured yet.</p>`;
        return;
    }

    els.publicFloorCards.innerHTML = state.floors.map(floor => `
        <button class="floor-card ${floor.id === state.selectedFloorId ? "active" : ""}" data-floor-id="${floor.id}">
            <h3>${escapeHtml(floor.name)}</h3>
            <p>${escapeHtml(floor.description || "No description yet.")}</p>
            <div class="floor-card-stats">
                <span class="stat-pill">${floor.bookedSeats}/${floor.totalSeats} booked</span>
                <span class="stat-pill">${floor.loadPercentage}% load</span>
            </div>
        </button>
    `).join("");

    els.publicFloorCards.querySelectorAll("[data-floor-id]").forEach(button => {
        button.addEventListener("click", async () => {
            state.selectedFloorId = Number(button.dataset.floorId);
            renderPublicFloors();
            await loadFloorLayout();
        });
    });
}

function renderSeatMap() {
    if (!state.floorLayout) {
        els.mapTitle.textContent = "Select a floor";
        els.seatLegend.innerHTML = "";
        els.seatMapFrame.innerHTML = `<p class="empty-state">Pick a floor card to see the layout.</p>`;
        renderSeatSelectionPanel();
        return;
    }

    els.mapTitle.textContent = `${state.floorLayout.floor.name} on ${formatDate(state.floorLayout.date)}`;
    els.seatLegend.innerHTML = state.floorLayout.legend.map(type => `
        <span class="legend-item">
            <span class="legend-swatch" style="background:${type.colorHex}"></span>
            ${escapeHtml(type.name)}
        </span>
    `).join("");

    const style = `grid-template-columns: repeat(${state.floorLayout.columns}, 52px);`;
    const cells = [];
    const seatMap = new Map(state.floorLayout.seats.map(seat => [`${seat.rowIndex}:${seat.columnIndex}`, seat]));
    for (let row = 1; row <= state.floorLayout.rows; row += 1) {
        for (let column = 1; column <= state.floorLayout.columns; column += 1) {
            const seat = seatMap.get(`${row}:${column}`);
            if (!seat) {
                cells.push(`<div class="layout-cell empty" aria-hidden="true"></div>`);
                continue;
            }
            const bookedClass = seat.booked ? "booked" : "";
            const tooltip = seat.booked
                ? `${seat.label} • ${seat.seatTypeName} • booked by ${seat.bookedByName}`
                : `${seat.label} • ${seat.seatTypeName}`;
            cells.push(`
                <button class="seat-button ${bookedClass}" data-seat-id="${seat.seatId}" title="${escapeHtml(tooltip)}" style="--seat-available:${seat.seatColorHex}">
                    <span>${escapeHtml(seat.label)}</span>
                </button>
            `);
        }
    }
    els.seatMapFrame.innerHTML = `<div class="seat-grid" style="${style}">${cells.join("")}</div>`;
    els.seatMapFrame.querySelectorAll("[data-seat-id]").forEach(button => {
        button.addEventListener("click", () => {
            const seatId = Number(button.dataset.seatId);
            state.selectedSeat = state.floorLayout.seats.find(seat => seat.seatId === seatId) || null;
            renderSeatSelectionPanel();
        });
    });
    renderSeatSelectionPanel();
}

function renderSeatSelectionPanel() {
    if (!state.selectedSeat || !state.floorLayout) {
        els.seatSelectionPanel.innerHTML = `<p class="empty-state">Select an available seat to book it, or a seat you booked to release it.</p>`;
        return;
    }

    const seat = state.selectedSeat;
    els.seatSelectionPanel.innerHTML = `
        <div>
            <p class="eyebrow">Selected seat</p>
            <h3>${escapeHtml(seat.label)} · ${escapeHtml(seat.seatTypeName)}</h3>
            <p>${seat.booked ? `Booked by ${escapeHtml(seat.bookedByName)}` : "Currently available"}</p>
        </div>
        <div class="selection-metadata">
            <div class="metadata-chip">Monitors: ${seat.hasMonitors ? "Yes" : "No"}</div>
            <div class="metadata-chip">Standing: ${seat.standing ? "Yes" : "No"}</div>
            <div class="metadata-chip">Chair: ${seat.requiresChair ? "Included" : "Not included"}</div>
            <div class="metadata-chip">Date: ${formatDate(state.floorLayout.date)}</div>
        </div>
        <div class="button-row">
            ${!seat.booked ? `<button class="primary-button" id="bookSeatButton">Book this seat</button>` : ""}
            ${seat.canCancel ? `<button class="ghost-button" id="cancelSeatButton">Release booking</button>` : ""}
        </div>
    `;

    document.querySelector("#bookSeatButton")?.addEventListener("click", handleBookSeat);
    document.querySelector("#cancelSeatButton")?.addEventListener("click", handleCancelSeat);
}

function renderDailyReport() {
    if (!state.dailyReport) {
        els.dailyReportBody.innerHTML = `<tr><td colspan="5">No report data available.</td></tr>`;
        return;
    }
    els.dailyReportBody.innerHTML = state.dailyReport.floors.map(floor => `
        <tr>
            <td>${escapeHtml(floor.name)}</td>
            <td>${floor.totalSeats}</td>
            <td>${floor.bookedSeats}</td>
            <td>${floor.availableSeats}</td>
            <td>${floor.loadPercentage}%</td>
        </tr>
    `).join("");
}

function updateHeroMetrics() {
    const totalAvailable = state.floors.reduce((sum, floor) => sum + floor.availableSeats, 0);
    const averageLoad = state.floors.length
        ? Math.round((state.floors.reduce((sum, floor) => sum + Number(floor.loadPercentage), 0) / state.floors.length) * 100) / 100
        : 0;
    els.heroAvailability.textContent = `${totalAvailable} seats`;
    els.heroLoad.textContent = `${averageLoad}%`;
}

async function handleBookSeat() {
    if (!state.selectedSeat) {
        return;
    }
    const bookedByName = els.viewerName.value.trim();
    if (!bookedByName) {
        setMessage(els.publicMessage, "Enter your name before booking a seat.", true);
        return;
    }
    try {
        await apiFetch("/api/public/bookings", {
            method: "POST",
            body: JSON.stringify({
                seatId: state.selectedSeat.seatId,
                bookingDate: state.publicDate,
                bookedByName
            })
        });
        setMessage(els.publicMessage, `Seat ${state.selectedSeat.label} booked successfully.`);
        await loadPublicExperience();
    } catch (error) {
        setMessage(els.publicMessage, error.message, true);
    }
}

async function handleCancelSeat() {
    if (!state.selectedSeat || !state.selectedSeat.bookingId) {
        return;
    }
    try {
        await apiFetch(`/api/public/bookings/${state.selectedSeat.bookingId}/cancel`, {
            method: "POST",
            body: JSON.stringify({requesterName: els.viewerName.value.trim()})
        });
        setMessage(els.publicMessage, `Seat ${state.selectedSeat.label} is available again.`);
        await loadPublicExperience();
    } catch (error) {
        setMessage(els.publicMessage, error.message, true);
    }
}

async function handleLoginStart(event) {
    event.preventDefault();
    try {
        const payload = await apiFetch("/api/auth/login", {
            method: "POST",
            body: JSON.stringify({
                username: els.adminUsername.value.trim(),
                password: els.adminPassword.value
            })
        });
        state.admin.challengeId = payload.challengeId;
        state.admin.otpDelivery = payload.delivery;
        state.admin.otpExpiresAt = payload.expiresAt;
        els.otpForm.classList.remove("hidden");
        const devCode = payload.devOtpCode ? ` Dev code: <strong>${escapeHtml(payload.devOtpCode)}</strong>.` : "";
        els.authSummary.innerHTML = `
            <p>OTP challenge created for <strong>${escapeHtml(payload.admin.fullName)}</strong>.</p>
            <p>Delivery: ${escapeHtml(payload.delivery)}. Expires at ${formatDateTime(payload.expiresAt)}.${devCode}</p>
        `;
        setMessage(els.adminMessage, "Enter the one-time code to complete sign-in.");
    } catch (error) {
        setMessage(els.adminMessage, error.message, true);
    }
}

async function handleOtpVerify(event) {
    event.preventDefault();
    try {
        const payload = await apiFetch("/api/auth/verify-otp", {
            method: "POST",
            body: JSON.stringify({
                challengeId: state.admin.challengeId,
                code: els.otpCode.value.trim()
            })
        });
        state.admin.token = payload.token;
        state.admin.profile = payload.admin;
        localStorage.setItem("seatsurfer-admin-token", payload.token);
        localStorage.setItem("seatsurfer-admin-profile", JSON.stringify(payload.admin));
        els.loginForm.reset();
        els.otpForm.reset();
        renderAdminState();
        await loadAdminWorkspace();
        setMessage(els.adminMessage, `Signed in as ${payload.admin.fullName}.`);
    } catch (error) {
        setMessage(els.adminMessage, error.message, true);
    }
}

async function restoreAdminSession() {
    try {
        const profile = await apiFetch("/api/auth/me", {}, true);
        state.admin.profile = profile;
        localStorage.setItem("seatsurfer-admin-profile", JSON.stringify(profile));
        renderAdminState();
        await loadAdminWorkspace();
    } catch (_error) {
        signOutAdmin();
    }
}

function renderAdminState() {
    const signedIn = Boolean(state.admin.token && state.admin.profile);
    els.adminWorkspace.classList.toggle("hidden", !signedIn);
    els.adminStatusChip.textContent = signedIn ? `Signed in: ${state.admin.profile.username}` : "Signed out";
    els.authSummary.innerHTML = signedIn
        ? `<p>Welcome back, <strong>${escapeHtml(state.admin.profile.fullName)}</strong>.</p>
           <div class="button-row"><button class="ghost-button" id="signOutButton" type="button">Sign out</button></div>`
        : `<p class="empty-state">Use the seeded admin credentials and the OTP flow to access management features.</p>`;
    document.querySelector("#signOutButton")?.addEventListener("click", signOutAdmin);
}

function signOutAdmin() {
    state.admin.token = "";
    state.admin.profile = null;
    state.admin.challengeId = null;
    localStorage.removeItem("seatsurfer-admin-token");
    localStorage.removeItem("seatsurfer-admin-profile");
    els.otpForm.classList.add("hidden");
    renderAdminState();
    setMessage(els.adminMessage, "Signed out.");
}

async function loadAdminWorkspace() {
    await Promise.all([loadAdminFloors(), loadSeatTypes()]);
    renderLayoutControls();
    await Promise.all([loadAdminLayoutIntoDraft(), loadAdminReports()]);
}

async function loadAdminFloors() {
    state.adminFloors = await apiFetch("/api/admin/floors", {}, true);
    renderAdminFloors();
}

function renderAdminFloors() {
    els.adminFloorList.innerHTML = state.adminFloors.map(floor => `
        <div class="collection-item">
            <div>
                <strong>${escapeHtml(floor.name)}</strong>
                <p>${escapeHtml(floor.description || "No description")}</p>
                <small>Sort order ${floor.sortOrder} · ${floor.active ? "Active" : "Inactive"}</small>
            </div>
            <div class="collection-actions">
                <button class="ghost-button" type="button" data-floor-edit="${floor.id}">Edit</button>
                <button class="ghost-button" type="button" data-floor-remove="${floor.id}">Deactivate</button>
            </div>
        </div>
    `).join("");

    els.layoutFloorSelect.innerHTML = state.adminFloors.map(floor => `
        <option value="${floor.id}">${escapeHtml(floor.name)}</option>
    `).join("");

    document.querySelectorAll("[data-floor-edit]").forEach(button => {
        button.addEventListener("click", () => {
            const floor = state.adminFloors.find(item => item.id === Number(button.dataset.floorEdit));
            if (!floor) {
                return;
            }
            els.floorId.value = String(floor.id);
            els.floorName.value = floor.name;
            els.floorDescription.value = floor.description || "";
            els.floorSortOrder.value = String(floor.sortOrder);
        });
    });

    document.querySelectorAll("[data-floor-remove]").forEach(button => {
        button.addEventListener("click", async () => {
            await apiFetch(`/api/admin/floors/${button.dataset.floorRemove}`, {method: "DELETE"}, true);
            await loadAdminFloors();
            await loadPublicExperience();
        });
    });
}

async function handleFloorSubmit(event) {
    event.preventDefault();
    const id = els.floorId.value.trim();
    const payload = {
        name: els.floorName.value.trim(),
        description: els.floorDescription.value.trim(),
        sortOrder: Number(els.floorSortOrder.value || 0),
        active: true
    };
    try {
        await apiFetch(id ? `/api/admin/floors/${id}` : "/api/admin/floors", {
            method: id ? "PUT" : "POST",
            body: JSON.stringify(payload)
        }, true);
        resetFloorForm();
        await loadAdminFloors();
        await loadPublicExperience();
        setMessage(els.adminMessage, "Floor saved successfully.");
    } catch (error) {
        setMessage(els.adminMessage, error.message, true);
    }
}

function resetFloorForm() {
    els.floorForm.reset();
    els.floorId.value = "";
    els.floorSortOrder.value = "0";
}

async function loadSeatTypes() {
    state.seatTypes = await apiFetch("/api/admin/seat-types", {}, true);
    renderSeatTypes();
}

function renderSeatTypes() {
    els.seatTypeList.innerHTML = state.seatTypes.map(type => `
        <div class="collection-item">
            <div>
                <strong>${escapeHtml(type.name)}</strong>
                <p>${escapeHtml(type.description || "No description")}</p>
                <small>${escapeHtml(type.code)} · ${type.active ? "Active" : "Inactive"}</small>
            </div>
            <div class="collection-actions">
                <span class="legend-item"><span class="legend-swatch" style="background:${type.colorHex}"></span>${escapeHtml(type.name)}</span>
                <button class="ghost-button" type="button" data-seat-type-edit="${type.id}">Edit</button>
                <button class="ghost-button" type="button" data-seat-type-remove="${type.id}">Deactivate</button>
            </div>
        </div>
    `).join("");

    document.querySelectorAll("[data-seat-type-edit]").forEach(button => {
        button.addEventListener("click", () => {
            const type = state.seatTypes.find(item => item.id === Number(button.dataset.seatTypeEdit));
            if (!type) {
                return;
            }
            els.seatTypeId.value = String(type.id);
            els.seatTypeCode.value = type.code;
            els.seatTypeName.value = type.name;
            els.seatTypeDescription.value = type.description || "";
            els.seatTypeColor.value = type.colorHex;
            els.seatTypeMonitors.checked = type.hasMonitors;
            els.seatTypeStanding.checked = type.standing;
            els.seatTypeChair.checked = type.requiresChair;
        });
    });

    document.querySelectorAll("[data-seat-type-remove]").forEach(button => {
        button.addEventListener("click", async () => {
            await apiFetch(`/api/admin/seat-types/${button.dataset.seatTypeRemove}`, {method: "DELETE"}, true);
            await loadSeatTypes();
            await loadPublicExperience();
        });
    });
}

async function handleSeatTypeSubmit(event) {
    event.preventDefault();
    const id = els.seatTypeId.value.trim();
    const payload = {
        code: els.seatTypeCode.value.trim(),
        name: els.seatTypeName.value.trim(),
        description: els.seatTypeDescription.value.trim(),
        colorHex: els.seatTypeColor.value,
        hasMonitors: els.seatTypeMonitors.checked,
        standing: els.seatTypeStanding.checked,
        requiresChair: els.seatTypeChair.checked,
        active: true
    };
    try {
        await apiFetch(id ? `/api/admin/seat-types/${id}` : "/api/admin/seat-types", {
            method: id ? "PUT" : "POST",
            body: JSON.stringify(payload)
        }, true);
        resetSeatTypeForm();
        await loadSeatTypes();
        await loadPublicExperience();
        renderLayoutControls();
        setMessage(els.adminMessage, "Seat type saved successfully.");
    } catch (error) {
        setMessage(els.adminMessage, error.message, true);
    }
}

function resetSeatTypeForm() {
    els.seatTypeForm.reset();
    els.seatTypeId.value = "";
    els.seatTypeColor.value = "#86b840";
    els.seatTypeChair.checked = true;
}

function renderLayoutControls() {
    if (!state.adminFloors.length) {
        els.layoutPalette.innerHTML = `<p class="empty-state">Create a floor and at least one seat type to design a layout.</p>`;
        return;
    }
    if (!els.layoutFloorSelect.value) {
        els.layoutFloorSelect.value = String(state.adminFloors[0].id);
    }
    if (!state.layoutDraft.selectedSeatTypeId && state.seatTypes.length) {
        state.layoutDraft.selectedSeatTypeId = state.seatTypes[0].id;
    }
    renderLayoutPalette();
    renderLayoutEditor();
}

function renderLayoutPalette() {
    const items = [
        `<button class="palette-item ${state.layoutDraft.selectedMode === "erase" ? "active" : ""}" data-palette-mode="erase">
            <span class="palette-swatch" style="background:#d5d9dc"></span>Erase
        </button>`
    ];
    state.seatTypes.filter(type => type.active).forEach(type => {
        items.push(`
            <button class="palette-item ${state.layoutDraft.selectedMode === "paint" && state.layoutDraft.selectedSeatTypeId === type.id ? "active" : ""}" data-seat-type-palette="${type.id}">
                <span class="palette-swatch" style="background:${type.colorHex}"></span>${escapeHtml(type.name)}
            </button>
        `);
    });
    els.layoutPalette.innerHTML = items.join("");

    document.querySelectorAll("[data-palette-mode='erase']").forEach(button => {
        button.addEventListener("click", () => {
            state.layoutDraft.selectedMode = "erase";
            renderLayoutPalette();
        });
    });

    document.querySelectorAll("[data-seat-type-palette]").forEach(button => {
        button.addEventListener("click", () => {
            state.layoutDraft.selectedMode = "paint";
            state.layoutDraft.selectedSeatTypeId = Number(button.dataset.seatTypePalette);
            renderLayoutPalette();
        });
    });
}

function renderLayoutEditor() {
    syncDraftDimensionsFromInputs();
    const style = `grid-template-columns: repeat(${state.layoutDraft.columns}, 52px);`;
    const cells = [];
    for (let row = 1; row <= state.layoutDraft.rows; row += 1) {
        for (let column = 1; column <= state.layoutDraft.columns; column += 1) {
            const key = `${row}:${column}`;
            const seatTypeId = state.layoutDraft.cells.get(key);
            const seatType = state.seatTypes.find(item => item.id === seatTypeId);
            const emptyClass = seatType ? "" : "empty";
            const color = seatType?.colorHex || "#d9dde0";
            cells.push(`
                <button class="layout-cell ${emptyClass}" data-layout-cell="${key}" style="--seat-available:${color}">
                    ${seatType ? `<span>${column}</span>` : ""}
                </button>
            `);
        }
    }
    els.layoutEditor.innerHTML = `<div class="layout-editor-grid" style="${style}">${cells.join("")}</div>`;
    els.layoutEditor.querySelectorAll("[data-layout-cell]").forEach(button => {
        button.addEventListener("click", () => {
            const key = button.dataset.layoutCell;
            if (state.layoutDraft.selectedMode === "erase") {
                state.layoutDraft.cells.delete(key);
            } else if (state.layoutDraft.selectedSeatTypeId) {
                state.layoutDraft.cells.set(key, state.layoutDraft.selectedSeatTypeId);
            }
            renderLayoutEditor();
        });
    });
}

function syncDraftDimensionsFromInputs() {
    state.layoutDraft.rows = Number(els.layoutRows.value || 1);
    state.layoutDraft.columns = Number(els.layoutColumns.value || 1);
    state.layoutDraft.cells = new Map(
        [...state.layoutDraft.cells.entries()].filter(([key]) => {
            const [row, column] = key.split(":").map(Number);
            return row <= state.layoutDraft.rows && column <= state.layoutDraft.columns;
        })
    );
}

async function loadAdminLayoutIntoDraft() {
    if (!els.layoutFloorSelect.value) {
        return;
    }
    try {
        const layout = await apiFetch(
            `/api/admin/floors/${els.layoutFloorSelect.value}/layout?date=${els.layoutEffectiveDate.value || today}`,
            {},
            true
        );
        state.layoutDraft.rows = Math.max(layout.rows, 1);
        state.layoutDraft.columns = Math.max(layout.columns, 1);
        els.layoutRows.value = String(state.layoutDraft.rows);
        els.layoutColumns.value = String(state.layoutDraft.columns);
        state.layoutDraft.cells = new Map(layout.seats.map(seat => [`${seat.rowIndex}:${seat.columnIndex}`, seat.seatTypeId]));
        renderLayoutControls();
    } catch (error) {
        setMessage(els.adminMessage, error.message, true);
    }
}

function clearDraft() {
    state.layoutDraft.cells = new Map();
    renderLayoutEditor();
}

async function saveDraftLayout() {
    if (!els.layoutFloorSelect.value) {
        setMessage(els.adminMessage, "Pick a floor before saving the layout.", true);
        return;
    }
    if (!state.layoutDraft.cells.size) {
        setMessage(els.adminMessage, "Add at least one seat to the layout before saving.", true);
        return;
    }
    try {
        const seats = [...state.layoutDraft.cells.entries()].map(([key, seatTypeId]) => {
            const [rowIndex, columnIndex] = key.split(":").map(Number);
            return {rowIndex, columnIndex, seatTypeId};
        });
        await apiFetch(`/api/admin/floors/${els.layoutFloorSelect.value}/layout`, {
            method: "PUT",
            body: JSON.stringify({
                effectiveFrom: els.layoutEffectiveDate.value || today,
                rows: state.layoutDraft.rows,
                columns: state.layoutDraft.columns,
                seats
            })
        }, true);
        await loadPublicExperience();
        setMessage(els.adminMessage, "Floor layout saved successfully.");
    } catch (error) {
        setMessage(els.adminMessage, error.message, true);
    }
}

async function loadAdminReports() {
    try {
        const [loadReport, monthlyReport] = await Promise.all([
            apiFetch(`/api/admin/reports/load?from=${els.loadFromDate.value}&to=${els.loadToDate.value}`, {}, true),
            apiFetch(`/api/admin/reports/monthly?year=${els.monthlyYear.value}&month=${els.monthlyMonth.value}`, {}, true)
        ]);
        state.loadReport = loadReport;
        state.monthlyReport = monthlyReport;
        renderAdminReports();
    } catch (error) {
        setMessage(els.adminMessage, error.message, true);
    }
}

function renderAdminReports() {
    if (state.loadReport) {
        els.loadReportPanel.innerHTML = `
            <div class="report-panel">
                <p class="eyebrow">Load report</p>
                <h3>${formatDate(state.loadReport.from)} to ${formatDate(state.loadReport.to)}</h3>
                <div class="load-grid">
                    ${state.loadReport.floors.map(floor => `
                        <article class="load-card">
                            <h4>${escapeHtml(floor.floorName)}</h4>
                            <p>Average load: <strong>${floor.averageLoadPercentage}%</strong></p>
                            <p>Peak load: <strong>${floor.peakLoadPercentage}%</strong></p>
                            <p>Booking volume: <strong>${floor.totalBookings}</strong></p>
                        </article>
                    `).join("")}
                </div>
            </div>
        `;
    }

    if (state.monthlyReport) {
        els.monthlyReportPanel.innerHTML = `
            <div class="report-panel">
                <p class="eyebrow">Monthly report</p>
                <h3>${state.monthlyReport.year}-${String(state.monthlyReport.month).padStart(2, "0")}</h3>
                <p>Completed bookings: <strong>${state.monthlyReport.totalCompletedBookings}</strong></p>
                <p>Cancellations: <strong>${state.monthlyReport.totalCancellations}</strong></p>
            </div>
        `;
    }
}

async function downloadPdfReport() {
    try {
        const blob = await apiFetch(
            `/api/admin/reports/export?date=${els.adminReportDate.value}&from=${els.loadFromDate.value}&to=${els.loadToDate.value}`,
            {},
            true
        );
        const url = URL.createObjectURL(blob);
        const anchor = document.createElement("a");
        anchor.href = url;
        anchor.download = `SeatSurfer-report-${els.adminReportDate.value}.pdf`;
        anchor.click();
        URL.revokeObjectURL(url);
    } catch (error) {
        setMessage(els.adminMessage, error.message, true);
    }
}

async function emailMonthlyReport() {
    try {
        await apiFetch(
            `/api/admin/reports/monthly/email?year=${els.monthlyYear.value}&month=${els.monthlyMonth.value}`,
            {method: "POST"},
            true
        );
        setMessage(els.adminMessage, "Monthly report email job triggered.");
    } catch (error) {
        setMessage(els.adminMessage, error.message, true);
    }
}

function setMessage(element, message, isError = false) {
    element.textContent = message;
    element.style.color = isError ? "var(--danger)" : "var(--muted)";
}

function formatDate(value) {
    return new Date(`${value}T00:00:00`).toLocaleDateString(undefined, {year: "numeric", month: "short", day: "numeric"});
}

function formatDateTime(value) {
    return new Date(value).toLocaleString();
}

function addDays(dateString, days) {
    const date = new Date(`${dateString}T00:00:00`);
    date.setDate(date.getDate() + days);
    return date.toISOString().slice(0, 10);
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#39;");
}
