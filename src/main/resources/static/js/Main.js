// ===== GLOBAL VARIABLES =====
let isLoading = false
let currentUser = null

// ===== DOCUMENT READY =====
document.addEventListener("DOMContentLoaded", () => {
  initializeApp()
  setupEventListeners()
  checkAuthStatus()
})

// ===== INITIALIZATION =====
function initializeApp() {
  // Add fade-in animation to main content
  const mainContent = document.querySelector(".main-content")
  if (mainContent) {
    mainContent.classList.add("fade-in")
  }

  // Initialize tooltips
  const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
  tooltipTriggerList.map((tooltipTriggerEl) => new bootstrap.Tooltip(tooltipTriggerEl))

  // Initialize popovers
  const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
  popoverTriggerList.map((popoverTriggerEl) => new bootstrap.Popover(popoverTriggerEl))
}

function setupEventListeners() {
  // Smooth scrolling for anchor links
  document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
    anchor.addEventListener("click", function (e) {
      e.preventDefault()
      const target = document.querySelector(this.getAttribute("href"))
      if (target) {
        target.scrollIntoView({
          behavior: "smooth",
          block: "start",
        })
      }
    })
  })

  // Back to top button
  const backToTopBtn = createBackToTopButton()
  document.body.appendChild(backToTopBtn)

  window.addEventListener("scroll", () => {
    if (window.pageYOffset > 300) {
      backToTopBtn.style.display = "block"
    } else {
      backToTopBtn.style.display = "none"
    }
  })

  // Search input enhancements
  const searchInputs = document.querySelectorAll(
    'input[type="search"], input[placeholder*="tìm kiếm"], input[placeholder*="Tìm kiếm"]',
  )
  searchInputs.forEach((input) => {
    input.addEventListener("input", debounce(handleSearchInput, 300))
  })

  // Form validation
  const forms = document.querySelectorAll("form")
  forms.forEach((form) => {
    form.addEventListener("submit", handleFormSubmit)
  })
}

function checkAuthStatus() {
  // Check if user is authenticated (this would typically be done server-side)
  const userElement = document.querySelector('[sec\\:authentication="name"]')
  if (userElement) {
    currentUser = userElement.textContent.trim()
  }
}

// ===== UTILITY FUNCTIONS =====
function debounce(func, wait) {
  let timeout
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout)
      func(...args)
    }
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
  }
}

function showLoading(element) {
  if (element) {
    element.innerHTML = `
            <div class="text-center py-4">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Đang tải...</span>
                </div>
            </div>
        `
  }
  isLoading = true
}

function hideLoading() {
  isLoading = false
}

function showToast(message, type = "info") {
  const toastContainer = getOrCreateToastContainer()
  const toast = createToast(message, type)
  toastContainer.appendChild(toast)

  const bsToast = new bootstrap.Toast(toast)
  bsToast.show()

  // Remove toast after it's hidden
  toast.addEventListener("hidden.bs.toast", () => {
    toast.remove()
  })
}

function getOrCreateToastContainer() {
  let container = document.getElementById("toast-container")
  if (!container) {
    container = document.createElement("div")
    container.id = "toast-container"
    container.className = "toast-container position-fixed top-0 end-0 p-3"
    container.style.zIndex = "1055"
    document.body.appendChild(container)
  }
  return container
}

function createToast(message, type) {
  const toast = document.createElement("div")
  toast.className = "toast"
  toast.setAttribute("role", "alert")
  toast.setAttribute("aria-live", "assertive")
  toast.setAttribute("aria-atomic", "true")

  const iconMap = {
    success: "fas fa-check-circle text-success",
    error: "fas fa-exclamation-circle text-danger",
    warning: "fas fa-exclamation-triangle text-warning",
    info: "fas fa-info-circle text-info",
  }

  toast.innerHTML = `
        <div class="toast-header">
            <i class="${iconMap[type] || iconMap.info} me-2"></i>
            <strong class="me-auto">Thông báo</strong>
            <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
        </div>
        <div class="toast-body">
            ${message}
        </div>
    `

  return toast
}

function createBackToTopButton() {
  const button = document.createElement("button")
  button.innerHTML = '<i class="fas fa-chevron-up"></i>'
  button.className = "btn btn-primary btn-floating back-to-top"
  button.style.cssText = `
        position: fixed;
        bottom: 20px;
        right: 20px;
        width: 50px;
        height: 50px;
        border-radius: 50%;
        display: none;
        z-index: 1000;
        box-shadow: 0 4px 12px rgba(0, 123, 255, 0.3);
        transition: all 0.3s ease;
    `

  button.addEventListener("click", () => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    })
  })

  button.addEventListener("mouseenter", function () {
    this.style.transform = "scale(1.1)"
  })

  button.addEventListener("mouseleave", function () {
    this.style.transform = "scale(1)"
  })

  return button
}

// ===== SEARCH FUNCTIONS =====
function handleSearchInput(event) {
  const input = event.target
  const query = input.value.trim()

  if (query.length > 2) {
    // Show search suggestions (if implemented)
    showSearchSuggestions(query, input)
  } else {
    hideSearchSuggestions()
  }
}

function showSearchSuggestions(query, inputElement) {
  // This would typically fetch suggestions from an API
  // For now, we'll just show some basic suggestions
  const suggestions = [query + " karaoke", query + " beat", query + " instrumental"]

  // Implementation would go here
}

function hideSearchSuggestions() {
  const suggestionContainer = document.getElementById("search-suggestions")
  if (suggestionContainer) {
    suggestionContainer.style.display = "none"
  }
}

// ===== FORM HANDLING =====
function handleFormSubmit(event) {
  const form = event.target
  const submitButton = form.querySelector('button[type="submit"]')

  if (submitButton && !form.checkValidity()) {
    event.preventDefault()
    event.stopPropagation()
    form.classList.add("was-validated")
    return false
  }

  if (submitButton) {
    const originalText = submitButton.innerHTML
    submitButton.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Đang xử lý...'
    submitButton.disabled = true

    // Re-enable button after 5 seconds (fallback)
    setTimeout(() => {
      submitButton.innerHTML = originalText
      submitButton.disabled = false
    }, 5000)
  }
}

// ===== VIDEO FUNCTIONS =====
function formatDuration(duration) {
  if (!duration) return ""

  // Parse ISO 8601 duration (PT4M13S)
  const match = duration.match(/PT(\d+H)?(\d+M)?(\d+S)?/)
  if (!match) return ""

  const hours = match[1] ? Number.parseInt(match[1]) : 0
  const minutes = match[2] ? Number.parseInt(match[2]) : 0
  const seconds = match[3] ? Number.parseInt(match[3]) : 0

  if (hours > 0) {
    return `${hours}:${minutes.toString().padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`
  } else {
    return `${minutes}:${seconds.toString().padStart(2, "0")}`
  }
}

function formatViewCount(count) {
  if (!count) return "0"
  const num = Number.parseInt(count)
  if (num >= 1000000000) {
    return (num / 1000000000).toFixed(1) + "B"
  } else if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + "M"
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + "K"
  }
  return num.toLocaleString("vi-VN")
}

function formatPublishedDate(dateString) {
  if (!dateString) return ""

  const date = new Date(dateString)
  const now = new Date()
  const diffTime = Math.abs(now - date)
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))

  if (diffDays === 1) {
    return "Hôm qua"
  } else if (diffDays < 7) {
    return `${diffDays} ngày trước`
  } else if (diffDays < 30) {
    const weeks = Math.floor(diffDays / 7)
    return `${weeks} tuần trước`
  } else if (diffDays < 365) {
    const months = Math.floor(diffDays / 30)
    return `${months} tháng trước`
  } else {
    const years = Math.floor(diffDays / 365)
    return `${years} năm trước`
  }
}

// ===== API FUNCTIONS =====
async function apiRequest(url, options = {}) {
  try {
    const response = await fetch(url, {
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
      },
      ...options,
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    return await response.json()
  } catch (error) {
    console.error("API request failed:", error)
    throw error
  }
}

// ===== PLAYLIST FUNCTIONS =====
async function addToPlaylist(videoId, title, thumbnail, channel) {
  if (!currentUser) {
    showToast("Vui lòng đăng nhập để sử dụng tính năng này", "warning")
    return
  }

  try {
    // This would show a modal to select playlist
    showPlaylistModal(videoId, title, thumbnail, channel)
  } catch (error) {
    console.error("Error adding to playlist:", error)
    showToast("Có lỗi xảy ra khi thêm vào playlist", "error")
  }
}

function showPlaylistModal(videoId, title, thumbnail, channel) {
  // Create and show playlist selection modal
  const modal = document.createElement("div")
  modal.className = "modal fade"
  modal.innerHTML = `
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-list me-2"></i>Thêm vào Playlist
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="video-info mb-3 p-3 bg-light rounded">
                        <div class="d-flex">
                            <img src="${thumbnail}" class="me-3" style="width: 80px; height: 60px; object-fit: cover; border-radius: 8px;">
                            <div>
                                <h6 class="mb-1">${title}</h6>
                                <small class="text-muted">${channel}</small>
                            </div>
                        </div>
                    </div>
                    <div id="playlistList">
                        <div class="text-center">
                            <div class="spinner-border spinner-border-sm"></div>
                            <p class="mt-2 mb-0">Đang tải playlist...</p>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    <button type="button" class="btn btn-primary" onclick="createNewPlaylist()">
                        <i class="fas fa-plus me-1"></i>Tạo playlist mới
                    </button>
                </div>
            </div>
        </div>
    `

  document.body.appendChild(modal)
  const bsModal = new bootstrap.Modal(modal)
  bsModal.show()

  // Load user playlists
  loadUserPlaylists(videoId, title, thumbnail, channel)

  // Clean up modal when hidden
  modal.addEventListener("hidden.bs.modal", () => {
    modal.remove()
  })
}

async function loadUserPlaylists(videoId, title, thumbnail, channel) {
  try {
    const data = await apiRequest("/api/playlists")
    const playlistList = document.getElementById("playlistList")

    if (data.playlists && data.playlists.length > 0) {
      playlistList.innerHTML = data.playlists
        .map(
          (playlist) => `
                <div class="playlist-item d-flex justify-content-between align-items-center p-2 border rounded mb-2">
                    <div>
                        <h6 class="mb-0">${playlist.name}</h6>
                        <small class="text-muted">${playlist.description || "Không có mô tả"}</small>
                    </div>
                    <button class="btn btn-outline-primary btn-sm" onclick="addVideoToPlaylist(${playlist.id}, '${videoId}', '${title.replace(/'/g, "\\'")}', '${thumbnail}', '${channel.replace(/'/g, "\\'")}')">
                        <i class="fas fa-plus"></i>
                    </button>
                </div>
            `,
        )
        .join("")
    } else {
      playlistList.innerHTML = `
                <div class="text-center text-muted">
                    <i class="fas fa-list fa-3x mb-3"></i>
                    <p>Bạn chưa có playlist nào</p>
                </div>
            `
    }
  } catch (error) {
    console.error("Error loading playlists:", error)
    document.getElementById("playlistList").innerHTML = `
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-triangle me-2"></i>
                Không thể tải danh sách playlist
            </div>
        `
  }
}

async function addVideoToPlaylist(playlistId, videoId, title, thumbnail, channel) {
  try {
    const response = await apiRequest(`/api/playlist/${playlistId}/add`, {
      method: "POST",
      body: JSON.stringify({
        videoId: videoId,
        videoTitle: title,
        videoThumbnail: thumbnail,
        channelName: channel,
      }),
    })

    if (response.success) {
      showToast("Đã thêm video vào playlist", "success")
      // Close modal
      const modal = bootstrap.Modal.getInstance(document.querySelector(".modal.show"))
      if (modal) modal.hide()
    } else {
      showToast(response.error || "Có lỗi xảy ra", "error")
    }
  } catch (error) {
    console.error("Error adding video to playlist:", error)
    showToast("Có lỗi xảy ra khi thêm video", "error")
  }
}

// ===== SHARE FUNCTIONS =====
function shareVideo(videoId, title) {
  const url = `${window.location.origin}/watch/${videoId}`

  if (navigator.share) {
    navigator
      .share({
        title: title,
        text: "Xem video karaoke này trên YouTube Karaoke",
        url: url,
      })
      .catch((err) => console.log("Error sharing:", err))
  } else {
    // Fallback to clipboard
    navigator.clipboard
      .writeText(url)
      .then(() => {
        showToast("Đã sao chép link video!", "success")
      })
      .catch(() => {
        // Fallback to manual copy
        showShareModal(url, title)
      })
  }
}

function showShareModal(url, title) {
  const modal = document.createElement("div")
  modal.className = "modal fade"
  modal.innerHTML = `
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-share me-2"></i>Chia sẻ video
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p class="mb-3">${title}</p>
                    <div class="input-group">
                        <input type="text" class="form-control" value="${url}" readonly id="shareUrl">
                        <button class="btn btn-outline-secondary" type="button" onclick="copyShareUrl()">
                            <i class="fas fa-copy"></i>
                        </button>
                    </div>
                    <div class="mt-3">
                        <p class="mb-2">Chia sẻ qua:</p>
                        <div class="d-flex gap-2">
                            <a href="https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(url)}" target="_blank" class="btn btn-primary btn-sm">
                                <i class="fab fa-facebook-f me-1"></i>Facebook
                            </a>
                            <a href="https://twitter.com/intent/tweet?url=${encodeURIComponent(url)}&text=${encodeURIComponent(title)}" target="_blank" class="btn btn-info btn-sm">
                                <i class="fab fa-twitter me-1"></i>Twitter
                            </a>
                            <a href="https://wa.me/?text=${encodeURIComponent(title + " " + url)}" target="_blank" class="btn btn-success btn-sm">
                                <i class="fab fa-whatsapp me-1"></i>WhatsApp
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `

  document.body.appendChild(modal)
  const bsModal = new bootstrap.Modal(modal)
  bsModal.show()

  modal.addEventListener("hidden.bs.modal", () => {
    modal.remove()
  })
}

function copyShareUrl() {
  const input = document.getElementById("shareUrl")
  input.select()
  document.execCommand("copy")
  showToast("Đã sao chép link!", "success")
}

// ===== KEYBOARD SHORTCUTS =====
document.addEventListener("keydown", (event) => {
  // Ctrl/Cmd + K for search
  if ((event.ctrlKey || event.metaKey) && event.key === "k") {
    event.preventDefault()
    const searchInput = document.querySelector(
      'input[type="search"], input[placeholder*="tìm kiếm"], input[placeholder*="Tìm kiếm"]',
    )
    if (searchInput) {
      searchInput.focus()
    }
  }

  // Escape to close modals
  if (event.key === "Escape") {
    const openModal = document.querySelector(".modal.show")
    if (openModal) {
      const modal = bootstrap.Modal.getInstance(openModal)
      if (modal) modal.hide()
    }
  }
})

// ===== ERROR HANDLING =====
window.addEventListener("error", (event) => {
  console.error("Global error:", event.error)
  // Don't show error toast for every error, only critical ones
})

window.addEventListener("unhandledrejection", (event) => {
  console.error("Unhandled promise rejection:", event.reason)
  // Handle promise rejections gracefully
})

// ===== PERFORMANCE OPTIMIZATION =====
// Lazy loading for images
if ("IntersectionObserver" in window) {
  const imageObserver = new IntersectionObserver((entries, observer) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        const img = entry.target
        img.src = img.dataset.src
        img.classList.remove("lazy")
        imageObserver.unobserve(img)
      }
    })
  })

  // Observe all lazy images
  document.querySelectorAll("img[data-src]").forEach((img) => {
    imageObserver.observe(img)
  })
}

// ===== EXPORT FUNCTIONS =====
window.YouTubeKaraoke = {
  formatDuration,
  formatViewCount,
  formatPublishedDate,
  addToPlaylist,
  shareVideo,
  showToast,
  apiRequest,
}
