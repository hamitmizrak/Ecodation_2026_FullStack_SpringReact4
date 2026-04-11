import { axiosClient } from '../../lib/axiosClient';
import { ENDPOINTS } from '../../config/api';

// LİST (BlogCategories)
export function listBlogCategories(params = {}) {
  return axiosClient.get(ENDPOINTS.BLOG_CATEGORY.LIST, { params });
}

// EKLE (BlogCategories)
export function createBlogCategories(payload) {
  return axiosClient.post(ENDPOINTS.BLOG_CATEGORY.CREATE, payload);
}

// GÜNCELLE (BlogCategories)
export function updateBlogCategories(id, payload) {
  return axiosClient.put(ENDPOINTS.BLOG_CATEGORY.UPDATE(id), payload);
}

// DELETE (BlogCategories)
export function deleteBlogCategories(id) {
  return axiosClient.delete(ENDPOINTS.BLOG_CATEGORY.DELETE(id));
}
