// BlogCategory.jsx (Final)
// Backend'e uyumlu: categoryId, categoryName, systemCreatedDate alanları
// Görsel alanları kaldırıldı (backend tarafında yok)
// Tabloda filtreleme + sıralama + sayfalama var
// Modal yönetimi; ESC ile kapanır; body scroll kilitlenir

import React, {useEffect, useMemo, useState} from 'react';
import axios from 'axios';
import {API_BASE, ENDPOINTS} from '../../config/api';
import {showSuccess, showError} from './resuability/toastHelper'; // Hata alt yapısı varsa kullan; yoksa console.log ile değiştir

// -------- Helpers --------
const extractData = (res) => {
    const d = res?.data;
    return d?.data ?? d?.result ?? d?.items ?? d?.content ?? d ?? [];
};

// Format Date
const fmtDate = (iso) =>
    !iso ? '' : new Date(iso).toLocaleString('tr-TR', {timeZone: 'Europe/Istanbul'});

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

// BlogCategory
export default function BlogCategory() {
    // ---- State ----
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(false);

    // Modals CRUD
    const [showCreate, setShowCreate] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [showView, setShowView] = useState(false);
    const [showDelete, setShowDelete] = useState(false);

    // Selection + Form
    const [selected, setSelected] = useState(null);
    const [form, setForm] = useState({categoryName: ''});
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

    // useEffect Hooks
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

    const sorted = useMemo(() => {
        const arr = [...filtered];
        arr.sort((a, b) => {
            const va =
                sortKey === 'categoryName'
                    ? (a.categoryName ?? '').toLowerCase()
                    : sortKey === 'systemCreatedDate'
                        ? new Date(a.systemCreatedDate || 0).getTime()
                        : (a.categoryId ?? a.id ?? 0);
            const vb =
                sortKey === 'categoryName'
                    ? (b.categoryName ?? '').toLowerCase()
                    : sortKey === 'systemCreatedDate'
                        ? new Date(b.systemCreatedDate || 0).getTime()
                        : (b.categoryId ?? b.id ?? 0);
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

    //////////////////////////////////////////////////////////
    // ---- Form Helpers ----
    const resetForm = () => {
        setForm({categoryName: ''});
        setFormError({});
    };

    // Close ALL
    const closeAll = () => {
        setShowCreate(false);
        setShowEdit(false);
        setShowView(false);
        setShowDelete(false);
    };

    // Open/Close CREATE
    const openCreate = () => {
        closeAll();
        resetForm();
        setShowCreate(true);
    };
    const closeCreate = () => {
        setShowCreate(false);
        resetForm();
    };

    // Open/Close EDIT
    const openEdit = (row) => {
        closeAll();
        setSelected(row);
        setForm({categoryName: row?.categoryName || ''});
        setFormError({});
        setShowEdit(true);
    };
    const closeEdit = () => {
        setShowEdit(false);
        setSelected(null);
        resetForm();
    };

    // Open/Close VIEW
    const openView = (row) => {
        closeAll();
        setSelected(row);
        setShowView(true);
    };
    const closeView = () => {
        setShowView(false);
        setSelected(null);
    };

    // Open/Close DELETE
    const openDelete = (row) => {
        closeAll();
        setSelected(row);
        setShowDelete(true);
    };
    const closeDelete = () => {
        setShowDelete(false);
        setSelected(null);
    };



    // ---- CRUD ----
    const onChange = (e) => {
        const {name, value} = e.target;
        setForm((p) => ({...p, [name]: value}));
        setFormError((p) => ({...p, [name]: undefined}));
    };

    const submitCreate = async (e) => {
        e.preventDefault();
        const err = {};
        if (!form.categoryName?.trim()) err.categoryName = 'Kategori adı zorunlu';
        setFormError(err);
        if (Object.keys(err).length) return;

        try {
            await axios.post(`${API_BASE}${ENDPOINTS.BLOG_CATEGORY.CREATE}`, {
                categoryName: form.categoryName.trim(),
            });
            showSuccess?.('Kategori eklendi.') ?? console.log('Kategori eklendi.');
            closeCreate();
            fetchList();
        } catch (ex) {
            showError?.(ex?.response?.data?.message || 'Kategori eklenemedi.') ?? console.error(ex);
            setFormError(ex?.response?.data?.validationErrors || {});
        }
    };

    const submitEdit = async (e) => {
        e.preventDefault();
        const err = {};
        if (!form.categoryName?.trim()) err.categoryName = 'Kategori adı zorunlu';
        setFormError(err);
        if (Object.keys(err).length) return;

        try {
            const id = selected?.categoryId ?? selected?.id;
            if (id == null) throw new Error('ID yok.');
            await axios.put(`${API_BASE}${ENDPOINTS.BLOG_CATEGORY.UPDATE(id)}`, {
                categoryName: form.categoryName.trim(),
            });
            showSuccess?.('Kategori güncellendi.') ?? console.log('Kategori güncellendi.');
            closeEdit();
            fetchList();
        } catch (ex) {
            showError?.(ex?.response?.data?.message || 'Kategori güncellenemedi.') ?? console.error(ex);
            setFormError(ex?.response?.data?.validationErrors || {});
        }
    };

    const confirmDelete = async () => {
        try {
            const id = selected?.categoryId ?? selected?.id;
            if (id == null) throw new Error('ID yok.');
            await axios.delete(`${API_BASE}${ENDPOINTS.BLOG_CATEGORY.DELETE(id)}`);
            showSuccess?.('Kategori silindi.') ?? console.log('Kategori silindi.');
            closeDelete();
            fetchList();
        } catch (ex) {
            showError?.(ex?.response?.data?.message || 'Silinemedi.') ?? console.error(ex);
        }
    };

    // ---- Render ----
    const SortBtn = ({k, children}) => (
        <button
            type="button"
            className="btn btn-link p-0 ms-1"
            title="Sırala"
            onClick={() => {
                if (sortKey === k) setSortDir((d) => (d === 'asc' ? 'desc' : 'asc'));
                else {
                    setSortKey(k);
                    setSortDir('asc');
                }
            }}
        >
            {children} {sortKey === k ? (sortDir === 'asc' ? '▲' : '▼') : ''}
        </button>
    );

    return (
        <React.Fragment>
            {/*CONTAINER*/}
            <div class="container py-4">

                {/*CREATE*/}
                <div class="d-flex align-items-center justify-content-between mb-3">
                    <h2 class="mb-0">Blog Kategori</h2>
                    <div class="d-flex gap-2">
                        <input
                            type="text"
                            class="form-control"
                            placeholder="Ara (ID/Ad)"
                            style={{minWidth: 220}}
                            value={query}
                            onChange={(e) => {
                                setQuery(e.target.value);
                                setPage(1);
                            }}
                        />
                        <button class="btn btn-primary" onClick={openCreate}>
                           <i className="fa fa-plus"></i> Yeni Kategori
                        </button>
                    </div>
                </div>

                {/* TABLE */}
                <div class="table-responsive">
                    <table className="table table-striped table-bordered align-middle">
                        {/*THEAD*/}
                        <thead>
                        <tr style={{width: 90}}>
                            ID <SortBtn temp="categoryId" className="me-1"/>
                        </tr>

                        <tr style={{width: 90}}>
                            KATEGORI ADI <SortBtn temp="categoryName" className="me-1"/>
                        </tr>

                        <tr style={{width: 220}}>
                            Oluşturma Tarihi <SortBtn temp="systemCreatedDate" className="me-1"/>
                        </tr>

                        <tr style={{width: 160}}>İşlemler</tr>
                        </thead>

                        {/*TBODY*/}
                        <tbody>
                        {loading ? (
                            <tr>
                                <td colspan={4} className="text-center">
                                    <span className="me-1 spinner-border spinner-border-sm"/>
                                    Yükleniyor...
                                </td>
                            </tr>
                        ) : paged.length === 0 ? (
                            <tr>
                                <td colspan={4} className="text-center text-muted">
                                    Kayıt Yok
                                </td>
                            </tr>
                        ) : (
                            paged.map((row) => (
                                <tr key={row.categoryId ?? row.id}>
                                    <td>{row.categoryId ?? row.id}</td>
                                    <td>{row.categoryName}</td>
                                    <td>{fmtDate(row.systemCreatedDate)}</td>
                                    <td>
                                            <div className="text-center btn-group btn-group-sm">

                                                {/*Göster*/}
                                                <button className="btn btn-outline-secondary" title="Detay" onClick={()=>openView(row)}>
                                                    <i className="fas fa-eye"></i>
                                                    {/*<i className="fas fa-edit"></i>*/}
                                                </button>


                                                {/*Düzenle*/}
                                                <button className="btn btn-outline-primary" title="Düzenle" onClick={()=>openEdit(row)}>
                                                    <i className="fas fa-edit"></i>
                                                   {/* <i className="fas fa-pen"></i>*/}
                                                </button>

                                                {/*Silme*/}
                                                <button className="btn btn-outline-danger" title="Sil" onClick={()=>openDelete(row)}>
                                                    <i className="fas fa-trash"></i>
                                                </button>

                                            </div>
                                     </td>
                                </tr>
                            ))
                        )}
                        </tbody>
                    </table>
                </div> {/*end table*/}

                {/*Pagination*/}
                <div className="d-flex align-items-center justify-content-between mt-2">
                    <div>
                        Toplam: <b>{total}</b> Kayıt, Sayfa <b>{currentPage}</b> / <b>{pageCount}</b>
                    </div>

                    <div className="d-flex align-items-center gap-2">
                        <select
                            style={{width: 90}}
                            className="form-select" value={pageSize} onChange={(e)=>{
                            setPageSize(parseInt(e.target.value || '10' , 10));
                            setPage(1)
                        }}>
                            {[5,10,20,50,100,200].map((n)=> (
                                <option key={n} value={n}>{n}/sayfa
                                </option>
                            ))}
                        </select>
                        <div className="btn-group">

                            {/*Geri tuşu*/}
                            <button
                                className="btn btn-outline-secondary"
                                disabled={currentPage <= 1}
                                onClick={() => setPage((p)=> Math.max(1, p-1))}
                            >
                                 Geri
                            </button>

                            {/*İleri tuşu*/}
                            <button className="btn btn-outline-danger"
                                    disabled={currentPage >= pageCount}
                                    onClick={() => setPage((p)=> Math.min(pageCount, p+1))}>
                                İleri
                            </button>
                        </div>
                    </div>
                </div> {/*end Pagination*/}

                {/*CREATE MODAL*/}
                {showCreate && (
                    <div
                        className="modal fade show d-block"
                    tabIndex={-1}
                        role="dialog"
                        style={{zIndex: 1050}}
                        onClick{{closeCreate}}>
                        <div
                            className="modal-dialog"
                            onClick={(e)=> e.stopPropagation()}>
                            <div className="modal-content">
                                <form onSubmit={submitCreate}>
                                    {/*HEADER*/}
                                    <div className="modal-header">
                                        <h5 className="modal-title">Yeni Kategori </h5>
                                        <button type="button" className"btn-close" onClick={closeCreate}/>
                                    </div>

                                    {/*BODY*/}
                                    <div className="modal-body"></div>
                                </form>
                            </div>

                        </div>
                    </div>
                )}


            </div>  {/*end container*/}
        </React.Fragment>
    );
}
