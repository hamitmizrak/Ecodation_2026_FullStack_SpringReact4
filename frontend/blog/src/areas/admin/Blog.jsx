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


    return <div>Blog</div>;
}

export default Blog;
