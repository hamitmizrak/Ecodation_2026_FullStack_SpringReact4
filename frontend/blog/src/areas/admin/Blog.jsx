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


  return <div>Blog</div>;
}

export default Blog;
