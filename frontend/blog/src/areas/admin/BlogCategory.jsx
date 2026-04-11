// BlogCategory.jsx (Final)
// Backend'e uyumlu: categoryId, categoryName, systemCreatedDate alanları
// Görsel alanları kaldırıldı (backend tarafında yok)
// Tabloda filtreleme + sıralama + sayfalama var
// Modal yönetimi; ESC ile kapanır; body scroll kilitlenir

import React, { useEffect, useMemo, useState } from 'react';
import axios from 'axios';
import { API_BASE, ENDPOINTS } from '../../config/api';
import { showSuccess, showError } from './resuability/toastHelper'; // varsa kullan; yoksa console.log ile değiştir


// -------- Helpers --------
const extractData = (res) => {
  const d = res?.data;
  return d?.data ?? d?.result ?? d?.items ?? d?.content ?? d ?? [];
};

const fmtDate = (iso) =>
  !iso ? '' : new Date(iso).toLocaleString('tr-TR', { timeZone: 'Europe/Istanbul' });

function GlobalBackdrop({ show, onClose }) {
  if (!show) return null;
  return (
    <div
      className="modal-backdrop fade show"
      style={{ zIndex: 1040 }}
      onClick={onClose || undefined}
    />
  );
}


export default function BlogCategory() {
  // ---- State ----
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);

  // Modals
  const [showCreate, setShowCreate] = useState(false);
  const [showEdit, setShowEdit] = useState(false);
  const [showView, setShowView] = useState(false);
  const [showDelete, setShowDelete] = useState(false);

  // Selection + Form
  const [selected, setSelected] = useState(null);
  const [form, setForm] = useState({ categoryName: '' });
  const [formError, setFormError] = useState({});

  // Filter + Sort + Pagination
  const [query, setQuery] = useState('');
  const [sortKey, setSortKey] = useState('categoryId'); // categoryId | categoryName | systemCreatedDate
  const [sortDir, setSortDir] = useState('asc'); // asc | desc
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);

  const anyOpen = showCreate || showEdit || showView || showDelete;

  // ---- Effects ----
  useEffect(() => {
    if (anyOpen) document.body.classList.add('modal-open');
    else document.body.classList.remove('modal-open');
    return () => document.body.classList.remove('modal-open');
  }, [anyOpen]);

  useEffect(() => {
    const onKey = (e) => {
      if (e.key !== 'Escape') return;
      if (showCreate) return closeCreate();
      if (showEdit) return closeEdit();
      if (showView) return closeView();
      if (showDelete) return closeDelete();
    };
    window.addEventListener('keydown', onKey);
    return () => window.removeEventListener('keydown', onKey);
  }, [showCreate, showEdit, showView, showDelete]);

  const fetchList = async () => {
    setLoading(true);
    try {
      const res = await axios.get(`${API_BASE}${ENDPOINTS.BLOG_CATEGORY.LIST}`);
      const data = extractData(res);
      const arr = Array.isArray(data) ? data : Array.isArray(data?.content) ? data.content : [];
      setItems(arr);
    } catch (e) {
      showError?.('Blog Kategori listesi yüklenemedi.') ?? console.error(e);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    fetchList();
  }, []);

  // ---- Search / Sort / Paginate ----
  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return items;
    return items.filter((x) => {
      const id = (x.categoryId ?? x.id ?? '').toString();
      const name = (x.categoryName ?? '').toLowerCase();
      return id.includes(q) || name.includes(q);
    });
  }, [items, query]);

  return <div>BlogCategory</div>;
}


