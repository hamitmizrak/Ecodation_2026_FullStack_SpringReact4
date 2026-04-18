// Blog.jsx — FINAL (octet-stream hatası çözülmüş sürüm)
// Özellikler: Arama | Sıralama | Sayfalama | Modal (ESC/backdrop) | Dosya önizleme/silme | Toast bildirimleri
// ÖNEMLİ: Multipart gönderimde header set ETME — tarayıcı boundary'i eklesin.
//         @RequestPart("blog") için JSON'u Blob ile 'application/json' tipinde gönder.


import React, {useEffect, useMemo, useState} from 'react';
import axios from 'axios';
import {API_BASE, ENDPOINTS, IMAGE_BASE} from '../../config/api';
import {showSuccess, showError} from './resuability/toastHelper';

// ---------- Helpers ----------
const extractData = (res) => {
    const d = res?.data;
    return d?.data ?? d?.result ?? d?.items ?? d?.content ?? d ?? [];
};

const fmtDate = (iso) =>
    !iso ? '' : new Date(iso).toLocaleString('tr-TR', {timeZone: 'Europe/Istanbul'});

const resolveImageUrl = (src) =>
    !src
        ? ''
        : /^https?:\/\//i.test(src)
            ? src
            : `${IMAGE_BASE}${src.startsWith('/') ? src : '/' + src}`;

function GlobalBackdrop({show, onClose}) {
    if (!show) return null;
    return (
        <div
            className="modal-backdrop fade show"
            style={{zIndex: 1040}}
            onClick={onClose || undefined}
        />
    );
}

///////////////////////////////////////////////////////////////
function Blog() {

    // Data
    const [items, setItems] = useState([]);
    const [cats, setCats] = useState([]);
    const [loading, setLoading] = useState(false);

    // Modals
    const [showCreate, setShowCreate] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [showView, setShowView] = useState(false);
    const [showDelete, setShowDelete] = useState(false);

    // Selection + Form
    const [selected, setSelected] = useState(null);
    const [form, setForm] = useState({
        header: '',
        title: '',
        content: '',
        image: '', // text URL (opsiyonel)
        categoryId: '', // dropdown
    });
    const [formError, setFormError] = useState({});


    // File state (multipart)
    const [file, setFile] = useState(null);
    const [filePreview, setFilePreview] = useState(''); // object URL

    // UX: filter/sort/page
    const [query, setQuery] = useState('');
    const [sortKey, setSortKey] = useState('blogId'); // blogId|header|title|categoryName|systemCreatedDate
    const [sortDir, setSortDir] = useState('asc');
    const [page, setPage] = useState(1);
    const [pageSize, setPageSize] = useState(10);

    const anyOpen = showCreate || showEdit || showView || showDelete;

    // ---------- Effects ----------
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

    // fetchBlogs
    const fetchBlogs = async () => {
        setLoading(true);
        try {
            const res = await axios.get(`${API_BASE}${ENDPOINTS.BLOG.LIST}`);
            const data = extractData(res);
            const arr = Array.isArray(data) ? data : Array.isArray(data?.content) ? data.content : [];
            setItems(arr);
        } catch (e) {
            showError?.('Blog listesi yüklenemedi.') ?? console.error(e);
        } finally {
            setLoading(false);
        }
    };

    const fetchCategories = async () => {
        try {
            const res = await axios.get(`${API_BASE}${ENDPOINTS.BLOG_CATEGORY.LIST}`);
            const data = extractData(res);
            const arr = Array.isArray(data) ? data : Array.isArray(data?.content) ? data.content : [];
            setCats(arr);
        } catch (e) {
            showError?.('Kategori listesi yüklenemedi.') ?? console.error(e);
        }
    };

    //
    useEffect(() => {
        fetchBlogs();  //.then(r => t.prototype).then();
        fetchCategories();
    }, []);


    // ---------- Derived (filter/sort/page) ----------
    const filtered = useMemo(() => {
        const q = query.trim().toLowerCase();
        if (!q) return items;
        return items.filter((x) => {
            const id = (x.blogId ?? x.id ?? '').toString();
            const header = (x.header ?? '').toLowerCase();
            const title = (x.title ?? '').toLowerCase();
            const catName = (x.blogCategoryDto?.categoryName ?? '').toLowerCase();
            return id.includes(q) || header.includes(q) || title.includes(q) || catName.includes(q);
        });
    }, [items, query]);

    const sorted = useMemo(() => {
        const arr = [...filtered];
        arr.sort((a, b) => {
            const va =
                sortKey === 'header'
                    ? (a.header ?? '').toLowerCase()
                    : sortKey === 'title'
                        ? (a.title ?? '').toLowerCase()
                        : sortKey === 'categoryName'
                            ? (a.blogCategoryDto?.categoryName ?? '').toLowerCase()
                            : sortKey === 'systemCreatedDate'
                                ? new Date(a.systemCreatedDate || 0).getTime()
                                : a.blogId ?? a.id ?? 0;
            const vb =
                sortKey === 'header'
                    ? (b.header ?? '').toLowerCase()
                    : sortKey === 'title'
                        ? (b.title ?? '').toLowerCase()
                        : sortKey === 'categoryName'
                            ? (b.blogCategoryDto?.categoryName ?? '').toLowerCase()
                            : sortKey === 'systemCreatedDate'
                                ? new Date(b.systemCreatedDate || 0).getTime()
                                : b.blogId ?? b.id ?? 0;
            const r = va < vb ? -1 : va > vb ? 1 : 0;
            return sortDir === 'asc' ? r : -r;
        });
        return arr;
    }, [filtered, sortKey, sortDir]);

    const total = sorted.length;
    const pageCount = Math.max(1, Math.ceil(total / pageSize));
    const currentPage = Math.min(page, pageCount);
    const paged = useMemo(() => {
        const start = (currentPage - 1) * pageSize;
        return sorted.slice(start, start + pageSize);
    }, [sorted, currentPage, pageSize]);


    // ---------- Form Helpers ----------
    const resetForm = () => {
        setForm({header: '', title: '', content: '', image: '', categoryId: ''});
        setFormError({});
        setFile(null);
        if (filePreview) {
            URL.revokeObjectURL(filePreview);
            setFilePreview('');
        }
    };

    // ---------- onChange ----------
    const onChange = (e) => {
        const {name, value} = e.target;
        setForm((p) => ({...p, [name]: value}));
        setFormError((p) => ({...p, [name]: undefined}));
    };


    // ---------- onFileChange ----------
    const onFileChange = (e) => {
        const f = e.target.files?.[0];
        if (!f) return clearFile();
        setFile(f);
        if (filePreview) URL.revokeObjectURL(filePreview);
        setFilePreview(URL.createObjectURL(f));
    };

    // ---------- clearFile ----------
    const clearFile = () => {
        setFile(null);
        if (filePreview) URL.revokeObjectURL(filePreview);
        setFilePreview('');
        const input = document.getElementById('blog-image-file');
        if (input) input.value = '';
    };

    // ---------- closeAll ----------
    const closeAll = () => {
        setShowCreate(false);
        setShowEdit(false);
        setShowView(false);
        setShowDelete(false);
    };

    // ---------- openCreate/closeCreate ----------
    const openCreate = () => {
        closeAll();
        resetForm();
        setShowCreate(true);
    };
    const closeCreate = () => {
        setShowCreate(false);
        resetForm();
    };

    // ---------- openEdit/closeEdit ----------
    const openEdit = (row) => {
        closeAll();
        setSelected(row);
        resetForm();
        setForm({
            header: row?.header || '',
            title: row?.title || '',
            content: row?.content || '',
            image: row?.image || '',
            categoryId: row?.blogCategoryDto?.categoryId ?? row?.blogCategoryDto?.id ?? '',
        });
        setShowEdit(true);
    };
    const closeEdit = () => {
        setShowEdit(false);
        setSelected(null);
        resetForm();
    };


    // ---------- openView/closeView ----------
    const openView = (row) => {
        closeAll();
        setSelected(row);
        setShowView(true);
    };
    const closeView = () => {
        setShowView(false);
        setSelected(null);
    };

    // ---------- openDelete/closeDelete ----------
    const openDelete = (row) => {
        closeAll();
        setSelected(row);
        setShowDelete(true);
    };
    const closeDelete = () => {
        setShowDelete(false);
        setSelected(null);
    };


    // ---------- Validate ----------
    const validateBlog = () => {
        const err = {};
        if (!form.header?.trim()) err.header = 'Header zorunlu';
        if (!form.title?.trim()) err.title = 'Başlık zorunlu';
        if (!form.content?.trim()) err.content = 'İçerik zorunlu';
        if (!form.categoryId) err.categoryId = 'Kategori seçiniz';
        return err;
    };

    // ---------- Build Payloads ----------
    const jsonBody = () => ({
        header: form.header.trim(),
        title: form.title.trim(),
        content: form.content.trim(),
        image: form.image?.trim() || 'resim.png',
        blogCategoryDto: {categoryId: Number(form.categoryId)},
    });

    // blog parçasını application/json olarak ekle (kritik!)
    const buildMultipart = () => {
        const fd = new FormData();
        const blob = new Blob([JSON.stringify(jsonBody())], {type: 'application/json'});
        fd.append('blog', blob);
        if (file) fd.append('file', file); // tip otomatik belirlenir (image/*)
        return fd;
    };

    // ---------- CRUD ----------
    const submitCreate = async (e) => {
        e.preventDefault();
        const err = validateBlog();
        setFormError(err);
        if (Object.keys(err).length) return;

        try {
            if (file) {
                // MULTIPART: header set ETME — tarayıcı boundary ekler
                await axios.post(`${API_BASE}${ENDPOINTS.BLOG.CREATE}`, buildMultipart());
            } else {
                // JSON
                await axios.post(`${API_BASE}${ENDPOINTS.BLOG.CREATE}`, jsonBody());
            }
            showSuccess?.('Blog eklendi.') ?? console.log('Blog eklendi.');
            closeCreate();
            fetchBlogs();
        } catch (ex) {
            const msg = ex?.response?.data?.message || ex?.message || 'Blog eklenemedi.';
            showError?.(msg) ?? console.error(ex);
            setFormError(ex?.response?.data?.validationErrors || {});
        }
    };

    const submitEdit = async (e) => {
        e.preventDefault();
        const err = validateBlog();
        setFormError(err);
        if (Object.keys(err).length) return;

        try {
            const id = selected?.blogId ?? selected?.id;
            if (id == null) throw new Error('Blog ID yok.');

            if (file) {
                // MULTIPART: header set ETME — tarayıcı boundary ekler
                await axios.put(`${API_BASE}${ENDPOINTS.BLOG.UPDATE(id)}`, buildMultipart());
            } else {
                // JSON
                await axios.put(`${API_BASE}${ENDPOINTS.BLOG.UPDATE(id)}`, jsonBody());
            }

            showSuccess?.('Blog güncellendi.') ?? console.log('Blog güncellendi.');
            closeEdit();
            fetchBlogs();
        } catch (ex) {
            const msg = ex?.response?.data?.message || ex?.message || 'Blog güncellenemedi.';
            showError?.(msg) ?? console.error(ex);
            setFormError(ex?.response?.data?.validationErrors || {});
        }
    };

    const confirmDelete = async () => {
        try {
            //alert("Silme alanı")
            const id = selected?.blogId ?? selected?.id;
            if (id == null) throw new Error('Blog ID yok.');
            await axios.delete(`${API_BASE}${ENDPOINTS.BLOG.DELETE(id)}`);
            showSuccess?.('Blog silindi.') ?? console.log('Blog silindi.');
            closeDelete();
            fetchBlogs();
        } catch (ex) {
            const msg = ex?.response?.data?.message || ex?.message || 'Silinemedi.';
            showError?.(msg) ?? console.error(ex);
        }
    };





    return <div>Blog</div>;
}

export default Blog;
